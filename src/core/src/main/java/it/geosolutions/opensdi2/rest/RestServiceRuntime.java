/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author alessio.fabiani
 *
 */
@JsonInclude(Include.NON_NULL)
public interface RestServiceRuntime {

	String getId();
	
	String getName();
	
	String getDescription();
	
	String getStatus();
	
	Float getProgress();
	
	Date getStartDate();
	
	Date getEndDate();
	
	List<RestItemParameter> getParameters();

}
