package it.geosolutions.opensdi2.ftp.user.geostore;

import it.geosolutions.opensdi2.ftp.user.AuthoritiesProvider;

import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.BaseUser;

/**
 * Wrapper Class for FTP user for GeoStore Users
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class GeoStoreFTPUser extends BaseUser implements User {
	private it.geosolutions.geostore.core.model.User gsUser;
	private AuthoritiesProvider authoritiesProvider;

	public GeoStoreFTPUser(it.geosolutions.geostore.core.model.User gsUser, AuthoritiesProvider authoritiesProvider){
		this.gsUser = gsUser;
		setEnabled(gsUser.isEnabled());
		setName(gsUser.getName());
		setAuthoritiesProvider(authoritiesProvider);
		
		setHomeDirectory(authoritiesProvider.getHomeDirectory(getName()));
		this.setAuthoritiesProvider(authoritiesProvider);
	}

	/**
	 * Set the authorities provider and the proper authorities/home directory
	 * @param authoritiesProvider
	 */
	private void setAuthoritiesProvider(AuthoritiesProvider authoritiesProvider) {
		this.authoritiesProvider = authoritiesProvider;
		if(authoritiesProvider instanceof GeoStoreUserAuthoritiesProvider){
			setAuthorities(   ((GeoStoreUserAuthoritiesProvider)authoritiesProvider).getAuthorities(this.gsUser)    );
			setHomeDirectory( ((GeoStoreUserAuthoritiesProvider)authoritiesProvider).getHomeDirectory(this.gsUser)  );
		}else{
			setAuthorities(  authoritiesProvider.getAuthorities(getName())  );
			setHomeDirectory(authoritiesProvider.getHomeDirectory(getName()));
		}
	}

	public it.geosolutions.geostore.core.model.User getGsUser() {
		return gsUser;
	}

	
	public AuthoritiesProvider getAuthoritiesProvider() {
		return authoritiesProvider;
	}

	

}
