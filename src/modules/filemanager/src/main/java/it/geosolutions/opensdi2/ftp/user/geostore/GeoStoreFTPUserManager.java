package it.geosolutions.opensdi2.ftp.user.geostore;

import it.geosolutions.geostore.core.model.enums.Role;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTUser;
import it.geosolutions.geostore.services.rest.model.UserList;
import it.geosolutions.opensdi2.ftp.user.AuthoritiesProvider;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.log4j.Logger;

/**
 * Custom Apache FTP UserManager for GeoStore users, using the GeoStoreClient.
 *         
 *         Requires an Administrator GeoStore client with admin role access
 *         to GeoStore.
 *         Allows to Set a proper <AuthoritiesProvider> object that provides 
 *         permissions for users. 
 *         NOTE: read only.
 *         
 * @author Lorenzo Natali, GeoSolutions 
 *
 */
public class GeoStoreFTPUserManager implements UserManager {
	

	private static final Logger LOGGER = Logger
			.getLogger(GeoStoreFTPUserManager.class);

	/**
	 * GeoStore Client to check users
	 */
	AdministratorGeoStoreClient client;
	
	/**
	 * AuthoritiesProvider
	 */
	private AuthoritiesProvider authoritiesProvider;
	
	/**
	 * Tries to do the login using a client generated on the fly
	 */
	@Override
	public User authenticate(Authentication authentication)
			throws AuthenticationFailedException {
		if (authentication instanceof UsernamePasswordAuthentication) {
			UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
			AdministratorGeoStoreClient client = new AdministratorGeoStoreClient();
			client.setUsername(auth.getUsername());
			client.setPassword(auth.getPassword());
			client.setGeostoreRestUrl(this.client.getGeostoreRestUrl());
			it.geosolutions.geostore.core.model.User gsUser = client
					.getUserDetails();
			User user = new GeoStoreFTPUser(gsUser, authoritiesProvider);
			
			
			return user;

		} else {
			LOGGER.error("Unrecognized authentication type: "
					+ authentication.getClass().getName());
			throw new AuthenticationFailedException(
					"Unrecognized authentication type: "
							+ authentication.getClass().getName());
		}
	}

	@Override
	public boolean doesExist(String name) throws FtpException {
		return client.getUser(name) != null;
	}

	@Override
	public String getAdminName() throws FtpException {
		// TODO Auto-generated method stub
		return client.getUsername();
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		UserList ul = client.getUsers();
		if (ul != null && ul.getList() != null) {
			String[] names = new String[ul.getList().size()];
			int i = 0;
			for (RESTUser u : ul.getList()) {
				names[i] = u.getName();
				i++;
			}
		}
		return null;
	}

	@Override
	public User getUserByName(String name) throws FtpException {
		it.geosolutions.geostore.core.model.User gsuser = client.getUser(name);
		if (gsuser != null) {
			User user = new GeoStoreFTPUser(gsuser,this.authoritiesProvider);
			return user;
			 
		}else{
			return null;
		}
		
	}

	

	@Override
	public boolean isAdmin(String name) throws FtpException {
		it.geosolutions.geostore.core.model.User user = client.getUser(name);
		if(user == null){
			return false;
		}
		return user.getRole() == Role.ADMIN;
	}
	
	
	// GETTERS AND SETTERS
	
	public AuthoritiesProvider getAuthoritiesProvider() {
		return authoritiesProvider;
	}

	public void setAuthoritiesProvider(AuthoritiesProvider authoritiesProvider) {
		this.authoritiesProvider = authoritiesProvider;
	}

	public AdministratorGeoStoreClient getClient() {
		return client;
	}

	public void setClient(AdministratorGeoStoreClient client) {
		this.client = client;
	}
	
	/**
	 * **NOTE** NOT IMPLEMENTED
	 */
	@Override
	public void save(User user) throws FtpException {
		//NOT IMPLEMENTED. READ ONLY

	}
	
	/**
	 * **NOTE** NOT IMPLEMENTED
	 */
	@Override
	public void delete(String name) throws FtpException {
		//NOT IMPLEMENTED. READ ONLY

	}

}
