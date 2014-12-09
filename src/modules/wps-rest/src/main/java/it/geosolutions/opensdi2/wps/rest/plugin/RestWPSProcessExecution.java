/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin;

import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.ows11.ExceptionReportType;
import net.opengis.ows11.impl.ExceptionTypeImpl;
import net.opengis.wps10.ExecuteResponseType;

import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;

/**
 * @author alessio.fabiani
 * 
 */
public class RestWPSProcessExecution implements RestServiceRuntime {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger
            .getLogger(RestWPSProcessExecution.class.getName());

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

    private Map<String, Object> details = new ConcurrentHashMap<String, Object>();

    private Map<String, Object> results = new ConcurrentHashMap<String, Object>();

    private ExceptionTypeImpl exception;

    private Object output;

    public RestWPSProcessExecution(String executionId, String name, String description,
            WebProcessingService wps, String statusLocation, String status, float progress,
            Date startDate, Date endDate) {
        this.executionId = executionId;
        this.name = name;
        this.description = description;
        this.wps = wps;
        this.statusLocation = statusLocation;

        if (status != null) {
            this.status = status;
        } else {
            this.status = "RUNNING";
        }

        this.progress = progress;
        this.startDate = startDate;
        this.endDate = endDate;

        try {
            /** Try to get the status from the WPS */
            final URL statusURL = new URL(statusLocation);
            this.executeResponse = wps.issueStatusRequest(statusURL).getExecuteResponse();

            if (this.executeResponse != null) {
                this.startDate = (Date) executeResponse.getStatus().getCreationTime();

                //??? updateRuntimeStatus();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not retrieve the process Execution Response.", e);

            // runtimeFailed();
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
        if (this.progress == 100.0f && (this.status == "SUCCESS" || this.status == "FAIL"))
            return this.progress;

        try {
            if (executeResponse != null && executeResponse.getStatus().getProcessStarted() != null) {
                this.progress = executeResponse.getStatus().getProcessStarted()
                        .getPercentCompleted().floatValue();
            } else if (executeResponse != null
                    && executeResponse.getStatus().getProcessAccepted() != null) {
                this.progress = 0.0f;
            }

            //??? updateRuntimeStatus();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception occurred while evaluating Process Status!", e);

            runtimeFailed();

            return 100.0f;
        }

        return progress;
    }

    /**
     * @return the statusLocation
     */
    public String getStatusLocation() {
        return statusLocation;
    }

    /**
	 * 
	 */
    protected void runtimeFailed() {
        this.endDate = new Date();
        this.status = "FAIL";
        this.progress = 100.0f;
    }

    /**
     * @throws MalformedURLException
     * @throws IOException
     * @throws ServiceException
     */
    public Object updateRuntimeStatus() throws MalformedURLException, IOException, ServiceException {
        if (this.output != null) {
            return this.output;
        }
        
        if (this.exception != null) {
            return this.exception;
        }
        
        // loop and wait for the process to be complete
        if (executeResponse != null && executeResponse.getStatus() != null) {
            if (executeResponse.getStatus().getProcessFailed() == null
                    && executeResponse.getStatus().getProcessSucceeded() == null) {

                URL url = new URL(statusLocation);
                ExecuteProcessResponse response = wps.issueStatusRequest(url);

                if (response.getExceptionResponse() != null) {
                    runtimeFailed();

                    exception = (ExceptionTypeImpl) response.getExceptionResponse().getException().get(0);
                    //String exceptionMessage = (String) exception.getExceptionText().get(0);
                    
                    return exception;
                } else {
                    executeResponse = response.getExecuteResponse();
                    progress = executeResponse.getStatus().getProcessStarted().getPercentCompleted().floatValue();
                }
            } else {
                progress = 100.0f;
                this.endDate = new Date();

                if (executeResponse.getStatus().getProcessFailed() != null) {
                    runtimeFailed();
                    ExceptionReportType exceptionReport = executeResponse.getStatus().getProcessFailed().getExceptionReport();
                    
                    if (exceptionReport != null) {
                        output = exceptionReport.getException().get(0);
                        
                        return output;
                    }
                } else {
                    this.status = "SUCCESS";
                }
                
                if(executeResponse.getProcessOutputs() != null && executeResponse.getProcessOutputs().getOutput() != null && 
                        !executeResponse.getProcessOutputs().getOutput().isEmpty()) {
                    output = executeResponse.getProcessOutputs().getOutput().get(0);
                }
                
                return output;
            }
        }
        
        return null;
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

    @Override
    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public Map<String, Object> getResults() {
        return results;
    }

}
