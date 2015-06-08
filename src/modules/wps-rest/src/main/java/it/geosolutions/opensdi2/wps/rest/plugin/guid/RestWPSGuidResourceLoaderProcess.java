/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package it.geosolutions.opensdi2.wps.rest.plugin.guid;

import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;
import it.geosolutions.opensdi2.wps.rest.plugin.CDATAEncoder;
import it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess;

import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.ows11.ExceptionType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.Wps10Factory;

import org.apache.log4j.Level;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wps.WebProcessingService;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.process.ProcessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * @author alessio.fabiani
 *
 */
public class RestWPSGuidResourceLoaderProcess extends RestWPSProcess {

    /**
     * @param serviceId
     * @param name
     * @param description
     * @param version
     * @param activeStatus
     */
    public RestWPSGuidResourceLoaderProcess(String serviceId, String name, String description,
            String version, String activeStatus) {
        super(serviceId, name, description, version, activeStatus);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#getRuntimes()
     */
    @Override
    public List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception {
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#execute(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String execute(Principal auth, String requestBody, Map<String, String> params)
            throws Exception {
        
        if (auth != null && auth instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) auth;

            if (user.isAuthenticated()) {
                // connect to the WPS
                WPSCapabilitiesType capabilities = wps.getCapabilities();

                // get the first process and execute it
                ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
                EList processes = processOfferings.getProcess();

                // does the server contain the specific process we want?
                boolean found = false;
                ProcessBriefType process = null;
                Iterator iterator = processes.iterator();
                while (iterator.hasNext()) {
                    process = (ProcessBriefType) iterator.next();
                    if (process.getIdentifier().getValue().equalsIgnoreCase(processIden)) {
                        found = true;

                        break;
                    }
                }

                // exit if my process doesn't exist on server
                if (!found) {
                    wps = null;
                    LOGGER.log(Level.ERROR, "WPS Process [" + processIden + "] not found!");
                    throw new ProcessException("WPS Process [" + processIden + "] not found!");
                }

                // do a full DescribeProcess on my process
                // http://geoserver.itc.nl:8080/wps100/WebProcessingService?REQUEST=DescribeProcess&IDENTIFIER=org.n52.wps.server.algorithm.collapse.SimplePolygon2PointCollapse&VERSION=1.0.0&SERVICE=WPS
                DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
                descRequest.setIdentifier(processIden);

                DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

                // based on the DescribeProcess, setup the execute
                ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
                ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
                iterator = pdt.getDataInputs().getInput().iterator();

                Map<String, InputDescriptionType> processInputs = new HashMap<String, InputDescriptionType>();
                while (iterator.hasNext()) {
                    InputDescriptionType idt = (InputDescriptionType) iterator.next();
                    processInputs.put(idt.getIdentifier().getValue(), idt);
                }

                /**
                 * Collect the Asset Allocator inputs
                 */
                List<String> layers = new ArrayList<String>();
                
                // based on the DescribeProcess, setup the execute
                CodeType resultsCodeType = Ows11Factory.eINSTANCE.createCodeType();
                resultsCodeType.setValue("result");
                
                String name = null;
                String description = null;

                // create the process Execution Request
                ExecuteProcessRequest guidExecRequest = wps.createExecuteProcessRequest();
                guidExecRequest.setIdentifier(processIden);

//                ExecuteType guidExecType = Wps10Factory.eINSTANCE.createExecuteType();
//                guidExecType.setVersion("1.0.0");
//                guidExecType.setService("WPS");
//                CodeType codeType = Ows11Factory.eINSTANCE.createCodeType();
//                codeType.setValue(processIden);
//
//                guidExecType.setIdentifier(codeType);
//
//                DataInputsType1 inputtypes = Wps10Factory.eINSTANCE.createDataInputsType1();
//                guidExecType.setDataInputs(inputtypes);
//
//                ResponseFormType oAAResponseForm = Wps10Factory.eINSTANCE
//                        .createResponseFormType();
//                OutputDefinitionType oAARawDataOutput = Wps10Factory.eINSTANCE
//                        .createOutputDefinitionType();
//                oAARawDataOutput.setIdentifier(resultsCodeType);
//                oAAResponseForm.setRawDataOutput(oAARawDataOutput);
//
//                guidExecType.setResponseForm(oAAResponseForm);
                
                // parse values from JSON
                requestBody = requestBody.trim().replaceAll("\n", "").replaceAll("\r", "");

                JsonFactory jsonF = new JsonFactory();
                JsonParser jsonP = jsonF.createParser(requestBody);
                jsonP.nextToken(); // will return JsonToken.START_OBJECT
                while (jsonP.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname = jsonP.getCurrentName();
                    jsonP.nextToken(); // move to value, or START_OBJECT/START_ARRAY

                    JsonToken token = jsonP.getCurrentToken();
                    if (token != JsonToken.START_OBJECT && token != JsonToken.START_ARRAY) {
                        // Value
                        if (fieldname == null)
                            continue;

                        if (fieldname.equals("name") && name == null) {
                            name = jsonP.getText();
                        } else if (fieldname.equals("description") && description == null) {
                            description = jsonP.getText();
                        } else if (processInputs.containsKey(fieldname)) {
                            String value = jsonP.getText();

//                            InputType input = Wps10Factory.eINSTANCE.createInputType();
//                            CodeType inputIdent = Ows11Factory.eINSTANCE.createCodeType();
//                            inputIdent.setValue(fieldname);
//                            input.setIdentifier(inputIdent);
//
//                            input.setData((net.opengis.wps10.DataType) wps.createLiteralInputValue(value));

//                            guidExecType.getDataInputs().getInput().add(input);
                            guidExecRequest.addInput(fieldname, Arrays.asList(wps.createLiteralInputValue(value)));
                        }
                    } else {
                        while (jsonP.nextToken() != JsonToken.END_ARRAY) {
                            // Parsing Layers GUID Rules
                            if (fieldname.equalsIgnoreCase("layers")) {
                                StringBuilder layerRule = extractJsonProperties(jsonP, "Layer");

                                layers.add(layerRule.toString());
                            }
                        }

                        if (layers != null && layers.size() > 0) {
                            List<EObject> layersInputData = new ArrayList<EObject>();
                            for (String layer : layers) {
                                ComplexDataType cdt = Wps10Factory.eINSTANCE.createComplexDataType();
                                cdt.getData().add(0, new CDATAEncoder(layer));
                                cdt.setMimeType("application/json");
                                net.opengis.wps10.DataType data = Wps10Factory.eINSTANCE.createDataType();
                                data.setComplexData(cdt);
                                layersInputData.add(data);
                                if (processInputs.containsKey("layer")) {
//                                  for (EObject aa : assetInputData) {
//                                      InputType input = Wps10Factory.eINSTANCE.createInputType();
//                                      CodeType inputIdent = Ows11Factory.eINSTANCE.createCodeType();
//                                      inputIdent.setValue("layer");
//                                      input.setIdentifier(inputIdent);
//                                      input.setData((net.opengis.wps10.DataType) aa);
//                                      
//                                      guidExecType.getDataInputs().getInput().add(input);
//                                  }
                                  guidExecRequest.addInput("layer", layersInputData);
                                }
                            }
                        }

                    }
                }
                jsonP.close();
                
                
                // Finally create the MapStore Config Execute Request chaining the Resource Loader one
                // and send an async exec request to the WPS
                ExecuteProcessResponse response = issueWPSSyncRequest(guidExecRequest, resultsCodeType);

                /**
                 * Handle Response
                 */
                // we should get a raw response, no exception, no response document
                ExecuteResponseType executeResponse = response.getExecuteResponse();
                while (executeResponse == null) {
                    executeResponse = response.getExecuteResponse();

                    final ExceptionReportType exceptionResponse = response
                            .getExceptionResponse();
                    if (exceptionResponse != null) {
                        ExceptionType exception = (ExceptionType) exceptionResponse
                                .getException().get(0);
                        String errorMessage = exception.getExceptionText().get(0).toString();
                        throw new ProcessException(errorMessage);
                    }

                    if (executeResponse == null) {
                        break;
                    }
                }
            }
        }
        
        return null;
    }

    @Override
    public String stop(Principal auth, RestServiceRuntime runtime, Map<String, String> params)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supportsQueries(Principal auth) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int countRuntimes(Principal auth) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<RestServiceRuntime> findRuntimes(Principal auth, String id, String status,
            Date startDate, Date endDate, Map<String, String> params, int page, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RestServiceRuntime getRuntime(Principal auth, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public synchronized boolean loadConfiguration(OSDIConfigurationKVP osdiConfigurationKVP) {
        this.processIden = (String) osdiConfigurationKVP.getValue("guid-wps-process.processIden");

        try {
            // testing connections
            if (this.geoServerUrl == null || 
                    !this.geoServerUrl.equals((String) osdiConfigurationKVP.getValue("process.geoserverUrl"))) {
                this.geoServerUrl  = (String) osdiConfigurationKVP.getValue("process.geoserverUrl");
                this.geoServerUser = (String) osdiConfigurationKVP.getValue("process.geoserverUsername");
                this.geoServerPwd  = (String) osdiConfigurationKVP.getValue("process.geoserverPassword");

                String wpsUrl = this.geoServerUrl + "/wps"; 
                this.url = new URL(wpsUrl);
                this.wps = new WebProcessingService(this.url);
            }

            this.wpsRestAPIGeoStoreAdminClient = new AdministratorGeoStoreClient();
            this.wpsRestAPIGeoStoreAdminClient.setGeostoreRestUrl((String) osdiConfigurationKVP.getValue("process.geostoreRestUrl"));
            this.wpsRestAPIGeoStoreAdminClient.setUsername((String) osdiConfigurationKVP.getValue("process.geostoreUsername"));
            this.wpsRestAPIGeoStoreAdminClient.setPassword((String) osdiConfigurationKVP.getValue("process.geostorePassword"));

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "WPS Service [" + getServiceId()
                    + "] could not be initialized due to an Exception!", e);
            setActiveStatus("DISABLED");

            return false;
        }

        return true;
    }
}
