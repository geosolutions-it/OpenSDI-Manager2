package it.geosolutions.npa.ftp;

import java.io.IOException;

import org.apache.ftpserver.filesystem.nativefs.impl.NativeFileSystemView;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;

import it.geosolutions.geostore.core.model.enums.Role;
import it.geosolutions.npa.download.NPADownloadService;
import it.geosolutions.npa.service.USIDService;
import it.geosolutions.opensdi2.ftp.user.geostore.GeoStoreFTPUser;

public class NPAFileSystemFactory implements FileSystemFactory{
	private USIDService usidService;
	private NPADownloadService downloadService;

	@Override
	public FileSystemView createFileSystemView(User user) throws FtpException {
		try {
			usidService.getUsidForRole("everyone");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user instanceof GeoStoreFTPUser){
			
			if (((GeoStoreFTPUser)user).getGsUser().getRole() == Role.ADMIN){
				
				return new NativeFileSystemView(user, false);
			}
			return new NPAFileSystemView(user, usidService,downloadService);
		}else{
			return null;
		}
	}
	public NPADownloadService getDownloadService() {
		return downloadService;
	}
	public void setDownloadService(NPADownloadService downloadService) {
		this.downloadService = downloadService;
	}
	
	public USIDService getUsidService() {
		return usidService;
	}

	public void setUsidService(USIDService usidService) {
		this.usidService = usidService;
	}

}
