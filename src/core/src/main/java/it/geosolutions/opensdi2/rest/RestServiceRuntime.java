/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.Date;
import java.util.List;

/**
 * @author alessio.fabiani
 *
 */
public interface RestServiceRuntime {

	String getId();
	
	String getName();
	
	String getDescriprion();
	
	String getStatus();
	
	Float getProgress();
	
	Date getStartDate();
	
	Date getEndDate();
	
	List<RestItemParameter> getParameters();

}
