package it.geosolutions.npa.ftp;

import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.npa.download.NPADownloadService;
import it.geosolutions.npa.service.USIDService;
import it.geosolutions.opensdi2.ftp.user.geostore.GeoStoreFTPUser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;
import org.apache.log4j.Logger;

public class NPAFileSystemView implements FileSystemView {

	private GeoStoreFTPUser user;

	Set<String> usids = new HashSet<String>();
	NPADownloadService downloadService;

	private String currentDir = "/";
	private final static Logger LOGGER = Logger
			.getLogger(NPAFileSystemView.class);

	/**
	 * Creates the file system view for the user
	 * @param user
	 * @param usidService
	 * @param downloadService 
	 */
	public NPAFileSystemView(User user, USIDService usidService, NPADownloadService downloadService) {
		if (user instanceof GeoStoreFTPUser) {
			this.user = (GeoStoreFTPUser) user;
			LOGGER.debug("creating FileSystemView for user " + user.getName());
			Set<UserGroup> groups = this.user.getGsUser().getGroups();
			this.downloadService = downloadService;
			loadUsids(user, usidService, groups);

			LOGGER.debug("");
		}
	}

	private void loadUsids(User user, USIDService usidService,
			Set<UserGroup> groups) {
		for (UserGroup group : groups) {
			String groupRole = getRoleFromGrup(group.getGroupName());
			try {
				for(Object o : usidService.getUsidForRole(groupRole)){
					usids.add((String)o);
				}
				
			} catch (IOException e) {
				LOGGER.error("ERROR GETTING USID FOR USER:" + user
						+ " group" + groupRole, e);
				throw new IllegalStateException(
						"Error getting usids from usidService", e);
			}
		}
	}

	@Override
	public boolean changeWorkingDirectory(String path) throws FtpException {
		// /(slash) represents the root directory
		// child directories are like folder1/file.zip , without the slash on start
		path = normalizePath(path);
		//check access 
		if (path.equals("/")) {
			currentDir = "/";
			return true;
		} 
		
		
		if(isAllowed(path)){
			currentDir = path;
			return true;
		}

		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public FtpFile getFile(String fileName) throws FtpException {
		

		String normalizedPath = normalizePath(fileName);
		//TODO if we want to provide recent orders orders
		/*
		 if (isHomeDirectory(fileName)) {
			return getHomeDirectory();
		} else if (isCurrentDir(fileName)) {
			return getLibraryFolder(currentDir);
		}
		if (isOrderFolder(normalizedPath)) {
			return getLibraryFolder(normalizedPath);
		}
		*/
		return getLibraryFolder(normalizedPath);
	}

	

	private FtpFile getLibraryFolder(String dir) {
		return new NPAFTPFile(usids,downloadService, "/".equals(normalizePath(dir)) ? null : dir);
	}

	@Override
	public FtpFile getHomeDirectory() throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpFile getWorkingDirectory() throws FtpException {
		return getLibraryFolder(currentDir);
	}

	@Override
	public boolean isRandomAccessible() throws FtpException {
		// TODO Auto-generated method stub
		return false;
	}

	private String getRoleFromGrup(String groupName) {
		return groupName.replace(" ", "_");
	}

	private boolean isAllowed(String path) {
		List<String> allowedPaths = this.downloadService.getFiles(usids);
		if(allowedPaths != null){
			for(String allowedPath : allowedPaths){
				if(allowedPath.startsWith(path)){
					return true;
				}
			}
		}
		return false;
		
	}

	private boolean isAbsolute(String fileName) {
		return fileName.startsWith("/") && fileName.length()>1;
	}

	private String normalizePath(String path) {
		if (path.equals("..")) {
			path = getParentDirPath(currentDir);
			if("".equals(path)){
				path = currentDir;
			}
		} else if (isAbsolute(path)) {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
		} else if (currentDir.equals("/")) {
			//TODO check it
			
		} 
		if (isCurrentDir(path)) {
			path = currentDir;
			return currentDir;
		}

		return path;
	}

	private boolean isCurrentDir(String fileName) {
		return fileName.equals("./");
	}

	private boolean isHomeDirectory(String fileName) {
		return fileName.equals("/")
				|| (isCurrentDir(fileName) && currentDir.equals("/"));
	}

	private String[] splitPath(String fileName) {
		return (isAbsolute(fileName) ? normalizePath(fileName)
				: getFullPath(fileName)).split("/");
	}

	private String getFullPath(String fileName) {
		if (currentDir.equals("/")) {
			return fileName;
		}

		return currentDir + '/' + fileName;
	}
	public static String getParentDirPath(String fileOrDirPath) {
		String start = "";
		if(fileOrDirPath == "/") return "/";
		if(fileOrDirPath.startsWith("/") && fileOrDirPath.length()>1){
			fileOrDirPath = fileOrDirPath.substring(1,fileOrDirPath.length());
			start = "/";
		}
		boolean endsWithSlash = fileOrDirPath.endsWith("/");
		if(endsWithSlash){
			fileOrDirPath = fileOrDirPath.substring(0,fileOrDirPath.length()-1);
		}
		if(!fileOrDirPath.contains("/")){
			return start;
		}
	    
	    return start + fileOrDirPath.substring(0, fileOrDirPath.lastIndexOf("/", fileOrDirPath.length() - 1));
	}

}
