/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSZ'Z'")
	Date getStartDate();
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSZ'Z'")
	Date getEndDate();
	
	List<RestItemParameter> getParameters();

	Map<String, Object> getDetails();
	
	Map<String, Object> getResults();
	
}
