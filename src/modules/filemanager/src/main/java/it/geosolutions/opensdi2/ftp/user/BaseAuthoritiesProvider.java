package it.geosolutions.opensdi2.ftp.user;

import java.util.List;


import org.apache.ftpserver.ftplet.Authority;
/**
 * Base Implementation of authorities provider that can 
 * keep static authorities valid for all the users.
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class BaseAuthoritiesProvider implements AuthoritiesProvider {

	List<Authority> authorities;
	
	String homeDirectory;
	
	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	
	
	@Override
	public List<Authority> getAuthorities(String userName) {
		return authorities;
	}

	@Override
	public String getHomeDirectory(String userName) {
		
		return homeDirectory;
	}

}
