package it.geosolutions.opensdi2.ftp.user.geostore;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.opensdi2.ftp.user.AuthoritiesProvider;

import java.util.List;

import org.apache.ftpserver.ftplet.Authority;

/**
 * Interface that manages authorities for GeoStoreUsers. This will be 
 * automatically recognized by the <GeoStoreFTPUser> to use these methods
 * implementation instead of the name-only methods
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public interface GeoStoreUserAuthoritiesProvider extends AuthoritiesProvider {

	public List<Authority> getAuthorities(User user);
	
	public String getHomeDirectory(User user);
}
