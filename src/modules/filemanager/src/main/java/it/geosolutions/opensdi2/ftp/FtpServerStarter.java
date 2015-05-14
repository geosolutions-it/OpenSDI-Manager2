package it.geosolutions.opensdi2.ftp;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.nio.NioListener;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Little Helper Bean, by default the embedded FTP won't start automatically and
 * the cleanest way to tart it through the Spring context is by creating a
 * custom bean that implements the DisposableBean interface and starts the
 * server at bean initialization.
 * 
 * @author Lorenzo Natali, GeoSolutions
 * @version 1.0
 *
 */
public class FtpServerStarter implements DisposableBean,
		ApplicationContextAware {

	/** Logger declaration */
	private static final Logger LOGGER = org.slf4j.LoggerFactory
			.getLogger(FtpServerStarter.class);

	/** The Ftp server to start and stop */
	private FtpServer server;

	/**
	 * The factory to create the server
	 */
	private FtpServerFactory serverFactory;

	/**
	 * The user manager 
	 */
	private UserManager userManager;
	
	
	/** 
	 * The fileSystem Factory
	 */
	private FileSystemFactory fileSystemFactory;
	
	/**
	 * The port to use for this server
	 * 
	 */
	int listenPort = 2121;

	
	/** Default constructor */
	private FtpServerStarter() {
		super();
	}

	/** Sets the FtpServer instance and starts the Ftp server */
	public void setFTPServer(FtpServer server) {
		this.server = server;
		init();
	}

	private void init() {
		try {
			server.start();

		} catch (Exception e) {
			throw new IllegalStateException("Unable to start the server.", e);
		}
	}

	/**
	 * Called when the Spring Application Context is being shutdown. Shuts down
	 * the Ftp Server.
	 */
	@Override
	public void destroy() throws Exception {
		server.stop();
		LOGGER.info("stopped Ftp server.");
	}

	public void start() throws FtpException {

		if (server == null && serverFactory != null) {
			try {
				// factory.setPort(listenPort);
				
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Starting FTP Server on port " + listenPort);
				}

				Listener listener = new NioListener(null, listenPort, false,
						null,
						new DataConnectionConfigurationFactory()
								.createDataConnectionConfiguration(), 300, null);
				
				serverFactory.addListener("default", listener);
				
				// sets the custom user manager to authenticate users
				serverFactory.setUserManager(getUserManager());
				
				// sets the custom file system manager to list and download files
				serverFactory.setFileSystem(getFileSystemFactory());
				server = serverFactory.createServer();
				server.start();
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Started FTP Server on port " + listenPort);
				}

			} catch (Throwable t) {
				LOGGER.error("Error starting Starting FTP", t);

			}
		} else {
			LOGGER.error("FTP Server already running");
			throw new FtpException("FTP Server already running");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		try {
			start();
			LOGGER.info("********************************************************");
			LOGGER.info("*** FTP Server Created and running on port:"+ listenPort +" ***");
			LOGGER.info("********************************************************");
		} catch (FtpException e) {
			
			LOGGER.error("*** ERROR *** Unable to Start server FTP SERVER ", e);
			
		}

	}
	
	//
	//GETTERS AND SETTERS
	//
	public int getListenPort() {
		return listenPort;
	}

	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public FtpServer getServer() {
		return server;
	}

	public void setServer(FtpServer server) {
		this.server = server;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public FileSystemFactory getFileSystemFactory() {
		return fileSystemFactory;
	}

	public void setFileSystemFactory(FileSystemFactory fileSystemFactory) {
		this.fileSystemFactory = fileSystemFactory;
	}

	public FtpServerFactory getServerFactory() {
		return serverFactory;
	}

	public void setServerFactory(FtpServerFactory serverFactory) {
		this.serverFactory = serverFactory;
	}

}