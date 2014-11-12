/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.wps10.ExecuteResponseType;

import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.response.ExecuteProcessResponse;

/**
 * @author alessio.fabiani
 * 
 */
public class RestWPSProcessExecution implements RestServiceRuntime {

	/**
	 * logger
	 */
	protected static final Logger LOGGER = Logger.getLogger(RestWPSProcessExecution.class.getName());
	
	private String executionId;
	private WebProcessingService wps;
	private Date startDate;
	private Date endDate;
	private ExecuteResponseType executeResponse;
	private String statusLocation;
	private float progress;
	private String status;

	private String name;

	private String description;

	public RestWPSProcessExecution(String executionId, String name, String description,
			WebProcessingService wps, String statusLocation) {
		this.executionId = executionId;
		this.name = name;
		this.description = description;
		this.wps = wps;
		this.statusLocation = statusLocation;
		
		try {
			URL statusURL = new URL(statusLocation);
			this.executeResponse = wps.issueStatusRequest(statusURL).getExecuteResponse();
			this.startDate = (Date) executeResponse.getStatus().getCreationTime();
			this.status = "RUNNING";
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Could not retrieve the process Execution Response.", e);
			
			this.progress = 100.0f;
			this.status = "FAIL";
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getId()
	 */
	@Override
	public String getId() {
		return this.executionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getStatus()
	 */
	@Override
	public String getStatus() {
		return this.status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getProgress()
	 */
	@Override
	public Float getProgress() {
		if (this.progress == 100.0f) return this.progress;
		
		try {
			if (executeResponse != null && executeResponse.getStatus().getProcessStarted() != null) {
				this.progress = executeResponse.getStatus().getProcessStarted().getPercentCompleted().floatValue(); 
			} else if (executeResponse != null && executeResponse.getStatus().getProcessAccepted() != null) {
				this.progress = 0.0f;
			}

			// loop and wait for the process to be complete
			if (executeResponse.getStatus().getProcessFailed() == null 
					&& executeResponse.getStatus().getProcessSucceeded() == null) {

				URL url = new URL(statusLocation);
				ExecuteProcessResponse response = wps.issueStatusRequest(url);

				if (response.getExceptionResponse() != null) {
					progress = 100.0f;
					this.endDate = new Date();
					this.status = "FAIL";
				} else {
					executeResponse = response.getExecuteResponse();
					progress = executeResponse.getStatus().getProcessStarted().getPercentCompleted().floatValue();
				}
			} else {
				progress = 100.0f;
				this.endDate = new Date();
				
				if (executeResponse.getStatus().getProcessFailed() != null) {
					this.status = "FAIL";
				} else {
					this.status = "SUCCESS";
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,  "Exception occurred while evaluating Process Status!", e);
			this.endDate = new Date();
			this.status = "FAIL";
			return 100.0f;
		}
		
		return progress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getStartDate()
	 */
	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getEndDate()
	 */
	@Override
	public Date getEndDate() {
		return this.endDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestServiceRuntime#getParameters()
	 */
	@Override
	public List<RestItemParameter> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the executionId
	 */
	public String getExecutionId() {
		return executionId;
	}

}
