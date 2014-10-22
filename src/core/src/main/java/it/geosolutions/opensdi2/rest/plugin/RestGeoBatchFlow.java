/**
 * 
 */
package it.geosolutions.opensdi2.rest.plugin;

import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestService;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author alessio.fabiani
 * 
 */
public class RestGeoBatchFlow implements RestService {

	private String serviceId;
	private String name;
	private String description;
	private String version;
	private String activeStatus;
	private List<RestItemParameter> parameters;
	private List<RestServiceRuntime> runtimes;

	/**
	 * @param serviceId
	 * @param description
	 * @param version
	 * @param activeStatus
	 */
	public RestGeoBatchFlow(String serviceId, String name, String description,
			String version, String activeStatus) {
		super();
		this.serviceId = serviceId;
		this.name = name;
		this.description = description;
		this.version = version;
		this.activeStatus = activeStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getServiceId()
	 */
	@Override
	public String getServiceId() {
		return serviceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getDescriprion()
	 */
	@Override
	public String getDescriprion() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getVersion()
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getActiveStatus()
	 */
	@Override
	public String getActiveStatus() {
		return activeStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getParameters()
	 */
	@Override
	public List<RestItemParameter> getParameters() {
		return parameters;
	}

	/**
	 * 
	 * @param parameters
	 */
	public void setParameters(List<RestItemParameter> parameters) {
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getRuntimes()
	 */
	@Override
	public List<RestServiceRuntime> getRuntimes() {
		return runtimes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#execute()
	 */
	@Override
	public String execute(Map<String, String> params) {
		runtimes = new ArrayList<RestServiceRuntime>();
		
		runtimes.add(new RestGeoBatchConsumer("65368e58-1133-4bf9-bb43-4b67b5769778", "", "", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("5c9ca5b3-af6d-4a5f-ba73-b495651e2f05", "", "", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("0882324e-0da9-47c4-99df-750193b93764", "", "", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("0c0070f5-7918-4094-904d-f0985a07fc68", "", "", "RUNNING", 37.5f, new Date(), null));
		runtimes.add(new RestGeoBatchConsumer("0882324e-0da9-47c4-99df-750193b93764", "", "", "SUCCESS", 100.0f, new Date(), new Date()));
		
		return "200";
	}

}
