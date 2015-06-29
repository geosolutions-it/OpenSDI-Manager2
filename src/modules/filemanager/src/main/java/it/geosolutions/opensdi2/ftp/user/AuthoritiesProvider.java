package it.geosolutions.opensdi2.ftp.user;
import java.util.List;

import org.apache.ftpserver.ftplet.Authority;

public interface AuthoritiesProvider {
	public List<Authority> getAuthorities(Object userName);
	public String getHomeDirectory(Object userName);
}
