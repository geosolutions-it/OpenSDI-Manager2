package it.geosolutions.npa.ftp;

import it.geosolutions.npa.download.NPADownloadService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ftpserver.ftplet.FtpFile;

/**
 * Implementation of file for NPA users
 * 
 * @author nali
 *
 */
public class NPAFTPFile implements FtpFile {
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	

	private String dir ="";
	private String file = "";
	private NPADownloadService service;
	private File fileObj;
	Set<String> usids;

	public NPAFTPFile(Set<String> usids, NPADownloadService service, String s) {
		this.usids = usids;
		this.service = service;
		this.file = s != null ? s : "";
		if (s != null) {
			this.fileObj = new File(service.getDownloadBase(), file);
		} else {
			this.fileObj = new File(service.getDownloadBase());
		}
	}

	@Override
	public InputStream createInputStream(long offset) throws IOException {
		if (!isReadable()) {
			throw new IOException("No read permission : " + fileObj.getName() );
		}
		// move to the appropriate offset and create input stream
		final RandomAccessFile raf = new RandomAccessFile(fileObj, "r");
		raf.seek(offset);
		return new FileInputStream(raf.getFD()) {
			 @Override
			 public void close() throws IOException {
				 super.close();
				 raf.close();
			 }
		};
	}

	@Override
	public OutputStream createOutputStream(long arg0) throws IOException {

		return null;
	}

	@Override
	public boolean delete() {

		return false;
	}

	@Override
	public boolean doesExist() {
		return this.fileObj.exists();
	}

	@Override
	public String getAbsolutePath() {
		return "/" + this.dir + this.file;
	}

	@Override
	public String getGroupName() {

		return null;
	}

	@Override
	public long getLastModified() {
		return this.fileObj.lastModified();
	}

	@Override
	public int getLinkCount() {
		return 0;
	}

	@Override
	public String getName() {
		return this.file;
	}

	@Override
	public String getOwnerName() {
		return null;
	}

	@Override
	public long getSize() {
		return this.fileObj.length();
	}

	@Override
	public boolean isDirectory() {
		return this.fileObj.isDirectory();
	}

	@Override
	public boolean isFile() {
		return fileObj.isFile();
	}

	@Override
	public boolean isHidden() {
		return fileObj.isHidden();
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isRemovable() {
		// do not allow remove
		return false;
	}

	@Override
	public boolean isWritable() {
		// do not allow write
		return false;
	}

	@Override
	public List<FtpFile> listFiles() {
		Set<FtpFile> files = new HashSet<FtpFile>();
		if (this.fileObj.isDirectory()) {
			List<String> all = this.service.getFiles(usids);  //TODO start from here
			
			for (String s : all) {
				//Base directory
				if( "".equals(this.file)){
					s = s.split(File.separator)[0];
					files.add(new NPAFTPFile(usids, service, s));
				//subdirectory files
				}else if (s.startsWith(this.file + "/") && !s.equals(this.file)) {
					s = s.split(this.file + "/")[1];
					files.add(new NPAFTPFile(usids, service, s));
				}
			}
		}
		return new ArrayList<FtpFile>(files);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NPAFTPFile other = (NPAFTPFile) obj;
		if (dir == null) {
			if (other.dir != null)
				return false;
		} else if (!dir.equals(other.dir))
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}

	@Override
	public boolean mkdir() {
		return false;
	}

	@Override
	public boolean move(FtpFile arg0) {
		return false;
	}

	@Override
	public boolean setLastModified(long arg0) {
		return false;
	}
	


}
