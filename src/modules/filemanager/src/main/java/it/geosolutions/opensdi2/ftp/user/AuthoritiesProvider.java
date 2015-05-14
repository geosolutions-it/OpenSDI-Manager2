package it.geosolutions.opensdi2.ftp.user;
import java.util.List;

import org.apache.ftpserver.ftplet.Authority;

public interface AuthoritiesProvider {
	public List<Authority> getAuthorities(String userName);
	public String getHomeDirectory(String userName);
}
