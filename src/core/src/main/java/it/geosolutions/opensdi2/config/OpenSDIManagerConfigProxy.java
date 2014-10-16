/**
 * 
 */
package it.geosolutions.opensdi2.config;

/**
 * @author alessio.fabiani
 *
 */
public interface OpenSDIManagerConfigProxy {

	Object getConfiguration(String scope, Class extensionPoint);
	
}
