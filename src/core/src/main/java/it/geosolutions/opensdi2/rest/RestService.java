/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.List;
import java.util.Map;

/**
 * @author alessio.fabiani
 *
 */
public interface RestService {
	
	String getServiceId();
	
	String getName();
	
	String getDescriprion();
	
	String getVersion();
	
	String getActiveStatus();
	
	List<RestItemParameter> getParameters();
	
	List<RestServiceRuntime> getRuntimes();

	String execute(Map<String, String> params);

}
