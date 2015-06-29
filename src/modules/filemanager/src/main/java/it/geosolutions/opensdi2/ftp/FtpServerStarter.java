package it.geosolutions.opensdi2.ftp;

import it.geosolutions.opensdi2.configurations.configdir.OpenSDIManagerConfig;

import java.io.File;

import javax.print.DocFlavor.URL;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
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
	 * Data Connection Configuration
	 */
	private DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();

	/**
         * Path to the Key Store File 
         */
        private String jksPath;

       /**
         * Password for the Key Store File 
         */
        private String jksPass = "password";

        /**
         * set implicit/explicit mode
         */
        private boolean implicitSsl = false;
        
	/**
	 * The port to use for this server
	 * 
	 */
	int listenPort = 2121;

    private ApplicationContext applicationContext;

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
			    FtpServerFactory serverFactory = new FtpServerFactory();
			    ListenerFactory factory = new ListenerFactory();

			    if (LOGGER.isInfoEnabled()) {
			        LOGGER.info("Starting FTP Server on port " + listenPort);
			    }

			    // set the port of the listener
			    factory.setPort(listenPort);

			    // replace the default listener
			    serverFactory.addListener("default", factory.createListener());

			    // SSL coniguration
			    SslConfigurationFactory ssl = new SslConfigurationFactory();

			    // A test jks has been provided, replace with your own
			    ssl.setKeystoreFile(new File(getJksPath()));
			    ssl.setKeystorePassword(getJksPass());

			    // set SSL configuration for the listener
			    factory.setSslConfiguration(ssl.createSslConfiguration());

			    // server will operate on standard FTP by default, it is 
			    // up to the client to start the encrypted session
			    factory.setImplicitSsl(isImplicitSsl());

			    // replace the default listener
			    serverFactory.addListener("default", factory.createListener());
			    serverFactory.setUserManager(getUserManager());
			    serverFactory.setFileSystem(getFileSystemFactory());

			    // start the server         
			    FtpServer server = serverFactory.createServer();
			    try{
			        server.start();
			    } catch(FtpException e){
			        if (LOGGER.isDebugEnabled()){
			            e.printStackTrace();
			        }
			    } 
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
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
	        this.applicationContext = applicationContext;
		try {
			start();
			LOGGER.info("********************************************************");
			LOGGER.info("*** FTP Server Created and running on port:"
					+ listenPort + " ***");
			LOGGER.info("********************************************************");
		} catch (FtpException e) {

			LOGGER.error("*** ERROR *** Unable to Start server FTP SERVER ", e);

		}

	}

	//
	// GETTERS AND SETTERS
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

	public String getJksPath() {
	    // use the tesing jks we provided by default
            java.net.URL location = this.getClass().getResource("/ftpserver.jks");
            if (location != null){
                jksPath = location.getPath();
                LOGGER.warn("*** OpenSDI is using the default keystore for the FTP server ***");
            }
            // otherwise look for a jks file in the config folder
            else{
                if(jksPath == null && applicationContext != null){
                    OpenSDIManagerConfig cf = (OpenSDIManagerConfig)applicationContext.getBean("baseConfig");
                    if(cf != null){
                        File confDir = cf.getConfigDir();
                        jksPath = new File(confDir,"ftpserver.jks").getAbsolutePath();
                    }
                }
            }

	    return jksPath;
	}

	public void setJksPath(String jksPath) {
	    this.jksPath = jksPath;
	}

	public String getJksPass() {
	    return jksPass;
	}

	public void setJksPass(String jksPass) {
	    this.jksPass = jksPass;
	}

	public boolean isImplicitSsl() {
	    return implicitSsl;
	}

	public void setImplicitSsl(boolean implicitSsl) {
	    this.implicitSsl = implicitSsl;
	}

	public DataConnectionConfigurationFactory getDataConnectionConfigurationFactory() {
		return dataConnectionConfigurationFactory;
	}

	public void setDataConnectionConfigurationFactory(
			DataConnectionConfigurationFactory dataConnectionConfigurationFactory) {
		this.dataConnectionConfigurationFactory = dataConnectionConfigurationFactory;
	}

}