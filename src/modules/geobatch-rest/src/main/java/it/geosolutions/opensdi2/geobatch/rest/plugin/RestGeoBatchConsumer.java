/**
 * 
 */
package it.geosolutions.opensdi2.geobatch.rest.plugin;

import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.util.Date;
import java.util.List;

/**
 * @author alessio.fabiani
 *
 */
public class RestGeoBatchConsumer implements RestServiceRuntime {

	private String id;
	private String name;
	private String description;
	private String status;
	private Float progress;
	private Date startDate;
	private Date endDate;
	private List<RestItemParameter> parameters;

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param status
	 * @param progress
	 * @param startDate
	 * @param endDate
	 */
	public RestGeoBatchConsumer(String id, String name, String description,
			String status, Float progress, Date startDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.progress = progress;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getStatus()
	 */
	@Override
	public String getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getProgress()
	 */
	@Override
	public Float getProgress() {
		return progress;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getStartDate()
	 */
	@Override
	public Date getStartDate() {
		return startDate;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getEndDate()
	 */
	@Override
	public Date getEndDate() {
		return endDate;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getParameters()
	 */
	@Override
	public List<RestItemParameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<RestItemParameter> parameters) {
		this.parameters = parameters;
	}

}
