/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    Set<RestService> getServices() throws Exception;

    boolean supportsQueries();

    int countServices();

    List<RestService> findServices(String serviceId, String name, String activeStatus,
            Map<String, String> params, int page, int pageSize);

    RestService getService(String serviceId);

    void setConfiguration(OSDIConfigurationKVP config);

    OSDIConfigurationKVP getConfiguration();
}
