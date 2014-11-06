/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.rest.RestService;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.io.IOException;
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
import org.geotools.ows.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

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

	@Autowired
	protected AdministratorGeoStoreClient wpsRestAPIGeoStoreAdminClient;
	
	protected URL url;

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
	private RestWPSProcess(String serviceId, String name, String description,
			String version, String activeStatus) {
		super(serviceId, name, description, version, activeStatus);
	}
	
	public RestWPSProcess(String serviceId, String name, String description,
			String version, String activeStatus, String wpsUrl, String processIden) {
		this(serviceId, name, description, version, activeStatus);
		try {
			//testing connections
			this.url = new URL(wpsUrl);
			//connect();
			this.processIden = processIden;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "WPS Service [" + serviceId + "] could not be initialized due to an Exception!", e);
			setActiveStatus("DISABLED");
		}
	}

	/**
	 * @return 
	 * @throws IOException
	 * @throws ServiceException
	 */
	protected WebProcessingService connect() throws IOException, ServiceException {
		WebProcessingService wps = new WebProcessingService(this.url);
		return wps;
	}

	@JsonIgnore public abstract List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception;

	@JsonIgnore public abstract String execute(Principal auth, String requestBody, Map<String, String> params) throws Exception;

	@JsonIgnore public abstract String stop(RestServiceRuntime runtime, Map<String, String> params) throws Exception;

	@JsonIgnore public abstract boolean supportsQueries();

	//@JsonIgnore public abstract List<RestServiceInfoParam> getInputs();

	//@JsonIgnore public abstract List<RestServiceInfoParam> getOutputs();

	@JsonIgnore public abstract int countRuntimes();

	@JsonIgnore public abstract List<RestServiceRuntime> findRuntimes(String id, String status,
			Date startDate, Date endDate, Map<String, String> params, int page,
			int pageSize);

	@JsonIgnore public abstract RestServiceRuntime getRuntime(String id);

}
