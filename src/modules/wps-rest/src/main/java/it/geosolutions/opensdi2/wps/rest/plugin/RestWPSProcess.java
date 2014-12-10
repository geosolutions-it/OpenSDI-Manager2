/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.rest.RestService;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.wps10.ResponseFormType;

import org.geotools.data.wps.WebProcessingService;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Alessio
 * 
 */
public abstract class RestWPSProcess extends RestService {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(RestWPSProcess.class.getName());

    protected AdministratorGeoStoreClient wpsRestAPIGeoStoreAdminClient;

    protected String geoServerUrl;
    
    protected String geoServerUser;
    
    protected String geoServerPwd;
    
    protected URL url;

    protected WebProcessingService wps;

    protected String processIden;

    protected ResponseFormType response;

    protected Map<String, RestServiceRuntime> runtimes = new ConcurrentHashMap<String, RestServiceRuntime>();

    /**
     * 
     * @param serviceId
     * @param name
     * @param description
     * @param version
     * @param activeStatus
     */
    protected RestWPSProcess(String serviceId, String name, String description, String version,
            String activeStatus) {
        super(serviceId, name, description, version, activeStatus);
    }

    @JsonIgnore
    public abstract List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception;

    @JsonIgnore
    public abstract String execute(Principal auth, String requestBody, Map<String, String> params)
            throws Exception;

    @JsonIgnore
    public abstract String stop(Principal auth, RestServiceRuntime runtime,
            Map<String, String> params) throws Exception;

    @JsonIgnore
    public abstract boolean supportsQueries(Principal auth);

    // @JsonIgnore public abstract List<RestServiceInfoParam> getInputs();

    // @JsonIgnore public abstract List<RestServiceInfoParam> getOutputs();

    @JsonIgnore
    public abstract int countRuntimes(Principal auth);

    @JsonIgnore
    public abstract List<RestServiceRuntime> findRuntimes(Principal auth, String id, String status,
            Date startDate, Date endDate, Map<String, String> params, int page, int pageSize);

    @JsonIgnore
    public abstract RestServiceRuntime getRuntime(Principal auth, String id);

    @Override
    public String getActiveStatus() {
        if ("DISABLED".equals(super.getActiveStatus())) {
            try {
                // testing connections
                this.wps = new WebProcessingService(this.url);
                setActiveStatus("ENABLED");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "WPS Service [" + getServiceId()
                        + "] could not be initialized due to an Exception!", e);
                setActiveStatus("DISABLED");
            }
        }

        return super.getActiveStatus();
    }

}
