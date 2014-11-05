/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfigExtensions;
import it.geosolutions.opensdi2.rest.RestPlugin;
import it.geosolutions.opensdi2.rest.RestService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alessio.fabiani
 *
 */
public class RestWPSPlugin implements RestPlugin {

	Set<RestService> wpsProcesses = Collections.newSetFromMap(new ConcurrentHashMap<RestService, Boolean>());
	
	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "wps";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getDescription()
	 */
	@Override
	public String getDescription() {
		return "WPS REST Plugin for OpenSDI2-Manager V.1.0";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getServices()
	 */
	@Override
	public Set<RestService> getServices() throws Exception {
		
		synchronized(wpsProcesses) {
			if (wpsProcesses.isEmpty()) {
				List<RestWPSProcess> wpsAvailableProcesses = OpenSDIManagerConfigExtensions.extensions(RestWPSProcess.class);
				
				if (wpsAvailableProcesses != null) {
					wpsProcesses.addAll(wpsAvailableProcesses);
				}
			}
		}
		
		return wpsProcesses;
	}

	@Override
	public boolean supportsQueries() {
		return false;
	}

	@Override
	public int countServices() {
		return 0;
	}

	@Override
	public List<RestService> findServices(String serviceId, String name,
			String activeStatus, Map<String, String> params, int page,
			int pageSize) {
		return null;
	}

	@Override
	public RestService getService(String serviceId) {
		return null;
	}

}
