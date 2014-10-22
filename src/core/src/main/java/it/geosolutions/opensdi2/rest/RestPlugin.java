/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.List;

/**
 * @author alessio.fabiani
 * 
 */
public interface RestPlugin {

	String getPluginName();

	String getDescription();

	String getVersion();

	List<RestService> getServices();

}
