/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author alessio.fabiani
 * 
 */
@JsonInclude(Include.NON_NULL)
public interface RestPlugin {

	String getPluginName();

	String getDescription();

	String getVersion();

	List<RestService> getServices();

}
