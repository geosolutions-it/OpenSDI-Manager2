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

import net.opengis.ows11.CodeType;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.Wps10Factory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * @author Alessio
 * 
 */
public abstract class RestWPSProcess extends RestService {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(RestWPSProcess.class.getName());

    /**
     * ATTENTION:
     * 
     * Be sure the GeoStore "gs_attribute.attribute_text" column has enough space to contain the text. By default GeoStore sets this column to
     * 'varchar(255)', which is very small.
     * 
     * @param message
     * @return
     */
    public static String sanitizeMessage(String message) {
        String sanitized = StringEscapeUtils.escapeSql(message);

        // sanitized = sanitized.replaceAll("\n", "").replaceAll("\r", "");

        return sanitized;
        // try {
        // return URLEncoder.encode(sanitized, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        // return e.getLocalizedMessage();
        // }
    }

    /**
     * 
     * @param value
     * @return
     */
    public static boolean isNumeric(String value) {
        boolean isNumber = false;
    
        try {
            Double.parseDouble(value);
            isNumber = true;
        } catch (Exception e) {
            isNumber = false;
        }
    
        return isNumber;
    }

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
                LOGGER.log(Level.ERROR, "WPS Service [" + getServiceId()
                        + "] could not be initialized due to an Exception!", e);
                setActiveStatus("DISABLED");
            }
        }

        return super.getActiveStatus();
    }

    /**
     * @param jsonP
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    protected StringBuilder extractJsonProperties(JsonParser jsonP, String objectId) throws IOException,
            JsonParseException {
                StringBuilder asset = new StringBuilder();
            
                asset.append("{\"" + objectId + "\": {");
            
                while (jsonP.nextToken() != JsonToken.END_OBJECT) {
                    String propertyname = jsonP.getCurrentName();
                    jsonP.nextToken(); // move to value, or START_OBJECT/START_ARRAY
            
                    if (jsonP.getCurrentToken() != JsonToken.START_OBJECT) {
            
                        asset.append("\"").append(propertyname).append("\":");
            
                        final String value = jsonP.getText();
                        if (!isNumeric(value))
                            asset.append("\"");
                        asset.append(value);
                        if (!isNumeric(value))
                            asset.append("\"");
                        asset.append(",");
                    } else if (jsonP.getCurrentToken() == JsonToken.START_OBJECT) {
            
                        asset.append("{\"").append(propertyname).append("\": {");
            
                        while (jsonP.nextToken() != JsonToken.END_OBJECT) {
                            propertyname = jsonP.getCurrentName();
                            jsonP.nextToken(); // move to value, or START_OBJECT/START_ARRAY
            
                            asset.append("\"").append(propertyname).append("\":");
            
                            final String value = jsonP.getText();
                            if (!isNumeric(value))
                                asset.append("\"");
                            asset.append(value);
                            if (!isNumeric(value))
                                asset.append("\"");
                            asset.append(",");
                        }
            
                        asset.deleteCharAt(asset.length() - 1);
                        asset.append("}");
                    }
                }
            
                asset.deleteCharAt(asset.length() - 1);
                asset.append("}}");
            
                return asset;
            }

    /**
     * @param exeRequest
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    protected ExecuteProcessResponse issueWPSAsyncRequest(ExecuteProcessRequest exeRequest, String outputType, String mimeType) throws IOException, ServiceException {
        // create the response type
        ResponseDocumentType doc = wps.createResponseDocumentType(false, true, true, outputType);
        DocumentOutputDefinitionType odt = (DocumentOutputDefinitionType) doc.getOutput().get(0);
        odt.setMimeType(mimeType);
        odt.setAsReference(true);
        ResponseFormType responseForm = wps.createResponseForm(doc, null);
        exeRequest.setResponseForm(responseForm);
    
        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);
        return response;
    }

    /**
     * @param exeRequest
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    protected ExecuteProcessResponse issueWPSSyncRequest(ExecuteProcessRequest exeRequest, CodeType resultsCodeType) throws IOException, ServiceException {
        // create the response type
        ResponseFormType responseForm = Wps10Factory.eINSTANCE
                .createResponseFormType();
        OutputDefinitionType resLoaderRawDataOutput = Wps10Factory.eINSTANCE
                .createOutputDefinitionType();
        resLoaderRawDataOutput.setIdentifier(resultsCodeType);
        responseForm.setRawDataOutput(resLoaderRawDataOutput);
        exeRequest.setResponseForm(responseForm);
    
        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);
        return response;
    }
}
