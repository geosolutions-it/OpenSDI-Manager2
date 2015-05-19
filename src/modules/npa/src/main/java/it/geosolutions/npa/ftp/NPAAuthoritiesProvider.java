package it.geosolutions.npa.ftp;

import java.util.List;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.enums.Role;
import it.geosolutions.npa.download.NPADownloadService;
import it.geosolutions.opensdi2.ftp.user.BaseAuthoritiesProvider;
import it.geosolutions.opensdi2.ftp.user.geostore.GeoStoreUserAuthoritiesProvider;

public class NPAAuthoritiesProvider extends BaseAuthoritiesProvider implements GeoStoreUserAuthoritiesProvider{
	
	private NPADownloadService downloadService;

	public NPADownloadService getDownloadService() {
		return downloadService;
	}

	public void setDownloadService(NPADownloadService downloadService) {
		this.downloadService = downloadService;
	}

	@Override
	public List<Authority> getAuthorities(User user) {
		 List<Authority> auths = super.getAuthorities(user.getName());
		if(user.getRole().equals(Role.ADMIN)){
			auths.add(new WritePermission());
		}
		return auths;
	}

	@Override
	public String getHomeDirectory(User user) {
		if(user.getRole().equals(Role.ADMIN)){
			return downloadService.getDownloadBase();
		}
		return super.getHomeDirectory();
	}
}
