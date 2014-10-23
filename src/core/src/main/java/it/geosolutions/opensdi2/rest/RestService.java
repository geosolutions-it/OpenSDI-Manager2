/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author alessio.fabiani
 *
 */
@JsonInclude(Include.NON_NULL)
public abstract class RestService {

	private String serviceId;
	private String name;
	private String description;
	private String version;
	private String activeStatus;
	private List<RestItemParameter> parameters;

	/**
	 * @param serviceId
	 * @param description
	 * @param version
	 * @param activeStatus
	 */
	@JsonCreator
	public RestService(String serviceId, String name, String description,
			String version, String activeStatus) {
		this.serviceId = serviceId;
		this.name = name;
		this.description = description;
		this.version = version;
		this.activeStatus = activeStatus;
	}

	/**
	 * 
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @return
	 */
	public String getActiveStatus() {
		return activeStatus;
	}

	/**
	 * 
	 * @return
	 */
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
	
	@JsonIgnore public abstract List<RestServiceRuntime> getRuntimes();

	@JsonIgnore public abstract String execute(Map<String, String> params);

	@JsonIgnore public abstract String stop(RestServiceRuntime runtime, Map<String, String> params);

}
