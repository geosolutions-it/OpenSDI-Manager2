package it.geosolutions.opensdi2.ftp.user.filesystem;

import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.User;

public class GeoStoreFileSystemView implements FileSystemView {

	public GeoStoreFileSystemView(User user) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean changeWorkingDirectory(String arg0) throws FtpException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public FtpFile getFile(String arg0) throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpFile getHomeDirectory() throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtpFile getWorkingDirectory() throws FtpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRandomAccessible() throws FtpException {
		// TODO Auto-generated method stub
		return false;
	}

}
