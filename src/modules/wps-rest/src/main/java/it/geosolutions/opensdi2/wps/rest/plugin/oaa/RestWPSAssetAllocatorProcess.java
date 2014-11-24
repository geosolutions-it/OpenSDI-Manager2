/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin.oaa;

import it.geosolutions.geostore.core.model.Attribute;
import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.SecurityRule;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.dto.ShortAttribute;
import it.geosolutions.geostore.services.dto.search.AndFilter;
import it.geosolutions.geostore.services.dto.search.BaseField;
import it.geosolutions.geostore.services.dto.search.CategoryFilter;
import it.geosolutions.geostore.services.dto.search.FieldFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.RESTSecurityRule;
import it.geosolutions.geostore.services.rest.model.RESTStoredData;
import it.geosolutions.geostore.services.rest.model.ResourceList;
import it.geosolutions.geostore.services.rest.model.SecurityRuleList;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;
import it.geosolutions.opensdi2.wps.rest.plugin.CDATAEncoder;
import it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess;
import it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcessExecution;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.ows11.ExceptionType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.Wps10Factory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.process.ProcessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * @author Alessio
 *
 */
public class RestWPSAssetAllocatorProcess extends RestWPSProcess {
	
	public enum RESTResourceVisibility {
		PRIVATE,
		SHARED,
		PUBLIC
	}

	private boolean inited;
	
	private String reasourceLoaderProcessIdent = "gs:ResourceLoader";

	/**
	 * @param serviceId
	 * @param name
	 * @param description
	 * @param version
	 * @param activeStatus
	 */
	public RestWPSAssetAllocatorProcess(String serviceId, String name,
			String description, String version, String activeStatus, String wpsUrl, String processIden) {
		super(serviceId, name, description, version, activeStatus, wpsUrl, processIden);
		this.inited = false;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#getRuntimes()
	 */
	@Override
	public List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception {
		
		// check if the process has been initialized or not
		initializeRuntimes(auth, null);
		
		return new ArrayList<RestServiceRuntime>(runtimes.values());
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#execute(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(Principal auth, String requestBody, Map<String, String> params) throws Exception {
		
		synchronized(runtimes) {
			/**
			 * http://localhost:8080/geoserver/ows?service=WPS&version=1.0.0&request=GetExecutionStatus&executionId=41d62e63-1f5d-4b28-8758-a09d23a5038d
			 * 
			 * http://localhost:9191/geostore/rest/resources/resource/{resourceId}/attributes
			 * http://localhost:9191/geostore/rest/resources/resource/{resourceId}/attributes/{name}
			 * 
			 * http://localhost:9191/geostore/rest/resources/resource/{resourceId}/permissions
			 */
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
			        while (iterator.hasNext())
			        {
			            process = (ProcessBriefType) iterator.next();
			            if (process.getIdentifier().getValue().equalsIgnoreCase( processIden ))
			            {
			                found = true;
			
			                break;
			            }
			        }
			
			        // exit if my process doesn't exist on server
			        if (!found)
			        {
			            LOGGER.log(Level.WARNING, "WPS Process [" + processIden + "] not found!");
			            return null;
			        }
			        
			        /**
			         * Collect the Asset Allocator inputs
			         */
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
			        while (iterator.hasNext())
			        {
			        	InputDescriptionType idt = (InputDescriptionType) iterator.next();
			        	processInputs.put(idt.getIdentifier().getValue(), idt);
			        }

			        // based on the DescribeProcess, setup the execute
//			        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
//			        exeRequest.setIdentifier(processIden);

			        // reference to the Cascaded Process
			        EObject wpsCascadeReference = Wps10Factory.eINSTANCE.createInputReferenceType();
			        ((InputReferenceType)wpsCascadeReference).setMimeType("application/xml");
			        ((InputReferenceType)wpsCascadeReference).setMethod(MethodType.POST_LITERAL);
			        ((InputReferenceType)wpsCascadeReference).setHref("http://geoserver/wps");

			        ExecuteType execType = Wps10Factory.eINSTANCE.createExecuteType();
			        execType.setVersion("1.0.0");
			        execType.setService("WPS");
			        CodeType codeType = Ows11Factory.eINSTANCE.createCodeType();
			        codeType.setValue(processIden);
			        
					execType.setIdentifier(codeType);
					
					DataInputsType1 inputtypes = Wps10Factory.eINSTANCE.createDataInputsType1();
					execType.setDataInputs(inputtypes);
			        
			        String name = null;
			        String description = null;

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
				    		if (fieldname == null) continue;

				    		if (fieldname.equals("name") && name == null) {
				    			name = jsonP.getText();
				    		} else if (fieldname.equals("description") && description == null) {
				    			description = jsonP.getText();
				    		} else if (processInputs.containsKey(fieldname)) {
				    			String value = jsonP.getText();
				    			
				    			if (fieldname.toLowerCase().endsWith("date")) {
				    				try {
				    					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				    					sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				    					Date date = sdf.parse(value);
				    					sdf.applyPattern("yyyyMMdd");
				    					value = sdf.format(date);
				    				} catch (Exception e) {
				    					LOGGER.log(Level.WARNING, "Was not possible to convert the [" + fieldname + "] in a suitable date forma!", e);
				    				}
				    			}
				    			
//				    			exeRequest.addInput(fieldname, Arrays.asList(wps.createLiteralInputValue(value)));
								InputType input = Wps10Factory.eINSTANCE.createInputType();
								CodeType inputIdent = Ows11Factory.eINSTANCE.createCodeType();
								inputIdent.setValue(fieldname);
								input.setIdentifier(inputIdent);
								
								input.setData((net.opengis.wps10.DataType) wps.createLiteralInputValue(value));
								
								execType.getDataInputs().getInput().add(input);
				    		}
				    	} else {
			    			List<String> assets = new ArrayList<String>();
				    		
				    		while (jsonP.nextToken() != JsonToken.END_ARRAY) {
				    			
				    			StringBuilder asset = new StringBuilder();
				    			
				    			asset.append("{\"Asset\": {");

				    			while (jsonP.nextToken() != JsonToken.END_OBJECT) {
				    				fieldname = jsonP.getCurrentName();
				    				jsonP.nextToken(); // move to value, or START_OBJECT/START_ARRAY
				    				
				    				if (jsonP.getCurrentToken() != JsonToken.START_OBJECT) {

				    					asset.append("\"").append(fieldname).append("\":");
				    					
				    					final String value = jsonP.getText();
				    					if (!isNumeric(value)) asset.append("\"");
				    						asset.append(value);
				    					if (!isNumeric(value)) asset.append("\"");
				    						asset.append(",");
				    				} else if (jsonP.getCurrentToken() == JsonToken.START_OBJECT) {
				    					
						    			asset.append("{\"").append(fieldname).append("\": {");
				    					
				    					while (jsonP.nextToken() != JsonToken.END_OBJECT) {
						    				fieldname = jsonP.getCurrentName();
						    				jsonP.nextToken(); // move to value, or START_OBJECT/START_ARRAY
						    				
					    					asset.append("\"").append(fieldname).append("\":");
					    					
					    					final String value = jsonP.getText();
					    					if (!isNumeric(value)) asset.append("\"");
					    						asset.append(value);
					    					if (!isNumeric(value)) asset.append("\"");
					    						asset.append(",");
				    					}
				    					
				    					asset.deleteCharAt(asset.length()-1);
				    					asset.append("}");
				    				}
				    			}
				    			
		    					asset.deleteCharAt(asset.length()-1);
		    					asset.append("}}");
		    					
				    			assets.add(asset.toString());
				    		}

				    		if (assets != null && assets.size() > 0) {
				    			List<EObject> assetInputData = new ArrayList<EObject>();
				    			for (String asset : assets) {
				    				ComplexDataType cdt = Wps10Factory.eINSTANCE.createComplexDataType();
				    				cdt.getData().add(0, new CDATAEncoder(asset));
				    				cdt.setMimeType("application/json");
				    				net.opengis.wps10.DataType data = Wps10Factory.eINSTANCE.createDataType();
				    				data.setComplexData(cdt);
				    				assetInputData.add(data);
				    			}
				    			if (processInputs.containsKey("asset")) {
//				    				exeRequest.addInput("asset", assetInputData);
									InputType input = Wps10Factory.eINSTANCE.createInputType();
									CodeType inputIdent = Ows11Factory.eINSTANCE.createCodeType();
									inputIdent.setValue("asset");
									input.setIdentifier(inputIdent);
									
									for (EObject aa : assetInputData) {
										input.setData((net.opengis.wps10.DataType) aa);
									}
									
									execType.getDataInputs().getInput().add(input);
				    			}
				    		}
				    		
			    		}
				    }
				    jsonP.close();
				    
				    // set the Cascade WPS process Body
			        ((InputReferenceType)wpsCascadeReference).setBody(execType);

				    // send an async exec request to the WPS
//			        ExecuteProcessResponse response = issueWPSAsyncRequest(exeRequest);
			        ExecuteProcessRequest resourceExeRequest = wps.createExecuteProcessRequest();
			        resourceExeRequest.setIdentifier(reasourceLoaderProcessIdent);
			        resourceExeRequest.addInput("resourcesXML", Arrays.asList(wpsCascadeReference));
			        ExecuteProcessResponse response = issueWPSAsyncRequest(resourceExeRequest);
			        
			        /**
			         * Handle Response
			         */
			        // we should get a raw response, no exception, no response document
			        ExecuteResponseType executeResponse = response.getExecuteResponse();
			        while (executeResponse == null) {
			        	executeResponse = response.getExecuteResponse();
			        	
			        	final ExceptionReportType exceptionResponse = response.getExceptionResponse();
						if ( exceptionResponse != null ) {
							ExceptionType exception = (ExceptionType) exceptionResponse.getException().get(0);
					        String errorMessage = exception.getExceptionText().get(0).toString();
			        		throw new ProcessException(errorMessage);
			        	}
			        }

			        /**
			         * Persist Resources on GeoStore
			         */
			        // persist the process input parameters into GeoStore
			        Long resourceId = persistToGeoStore(auth, requestBody, name, description, executeResponse);
				}
			}
		}
		
        return null;
	}

	private static boolean isNumeric(String value) {
		boolean isNumber = false;
		
		try {
			Double.parseDouble(value);
			isNumber = true;
		} catch(Exception e) {
			isNumber = false;
		}
		
		return isNumber;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#stop(it.geosolutions.opensdi2.rest.RestServiceRuntime, java.util.Map)
	 */
	@Override
	public String stop(Principal auth, RestServiceRuntime runtime, Map<String, String> params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#supportsQueries()
	 */
	@Override
	public boolean supportsQueries(Principal auth) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#countRuntimes()
	 */
	@Override
	public int countRuntimes(Principal auth) {
		return runtimes.size();
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#findRuntimes(java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Map, int, int)
	 */
	@Override
	public List<RestServiceRuntime> findRuntimes(Principal auth, String id, String status,
			Date startDate, Date endDate, Map<String, String> params, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#getRuntime(java.lang.String)
	 */
	@Override
	public RestServiceRuntime getRuntime(Principal auth, String id) {
		
		// check if the process has been initialized or not
		RestServiceRuntime runtime = runtimes.get(id);
		runtime = initializeRuntimes(auth, runtime);
		
		return runtime;
	}

	/**
	 * Initialize Runtimes
	 * 
	 * @param auth
	 * @param restServiceRuntime 
	 * @return 
	 */
	protected RestServiceRuntime initializeRuntimes(Principal auth, RestServiceRuntime restServiceRuntime) {
		synchronized(runtimes) {
			if (!inited) {
				// Initialize Runtimes from GeoStore

				// sync runtimes stored on GeoStore
				AndFilter searchFilter = new AndFilter();
				searchFilter.add(new CategoryFilter("WPS_RUN_CONFIGS", SearchOperator.EQUAL_TO));
				searchFilter.add(new FieldFilter(BaseField.METADATA, getServiceId(), SearchOperator.EQUAL_TO));
				ResourceList resources = wpsRestAPIGeoStoreAdminClient.searchResources(searchFilter, -1, -1, true, true);

				if ( resources != null && !resources.isEmpty() ) {
					for ( Resource r : resources.getList() ) {
						
						// Security check
						//r = wpsRestAPIGeoStoreAdminClient.getResource(r.getId(), true);
						SecurityRuleList secRules = wpsRestAPIGeoStoreAdminClient.getSecurityRules(r.getId());
						boolean allowed = false;
						if (secRules != null && !secRules.getList().isEmpty()) {
							allowed = isAllowed(auth, secRules);
						} else {
							allowed = false;
						}
						
						if ( allowed ) {
							final String executionId = r.getName();
							
							String owner = null;
							String name = null;
							String description = null;
							String statusLocation = null;
							String status = null;
							Date startDate = null;
							float progress = 0;
							final RestWPSProcessExecution runtime = extractRuntimeFromGeoStore(
									r, executionId, owner, name, description,
									statusLocation, status, startDate, progress);
							
							runtimes.put(executionId, runtime);

							if (owner == null) {
								owner = extractOwner(secRules);
							}
							runtime.getDetails().put("owner", owner);
							runtime.getDetails().put("resourceId", r.getId());
							runtime.getDetails().put("resourceVisibility", extractVisibility(secRules));
							runtime.getDetails().put("message", "");
							
							runtime.getResults().put("mapId", 5800);
						}
					}
				}
				inited = true;
			} else {
				// Update the restServiceRuntime Status

				// sync runtimes stored on GeoStore
				AndFilter searchFilter = new AndFilter();
				searchFilter.add(new CategoryFilter("WPS_RUN_CONFIGS", SearchOperator.EQUAL_TO));
				if (restServiceRuntime != null) {
					searchFilter.add(new FieldFilter(BaseField.NAME, restServiceRuntime.getName(), SearchOperator.EQUAL_TO));
				}
				searchFilter.add(new FieldFilter(BaseField.METADATA, getServiceId(), SearchOperator.EQUAL_TO));
				ResourceList resources = wpsRestAPIGeoStoreAdminClient.searchResources(searchFilter, -1, -1, true, true);

				if ( resources != null && !resources.isEmpty() ) {
					for ( Resource r : resources.getList() ) {
						
						// Security check
						//r = wpsRestAPIGeoStoreAdminClient.getResource(r.getId(), true);
						SecurityRuleList secRules = wpsRestAPIGeoStoreAdminClient.getSecurityRules(r.getId());
						boolean allowed = false;
						if (secRules != null && !secRules.getList().isEmpty()) {
							allowed = isAllowed(auth, secRules);
						} else {
							allowed = false;
						}
						
						final String executionId = r.getName();
						RestWPSProcessExecution runtime = null;
						if ( allowed ) {
							if (restServiceRuntime == null) {
								runtime = (RestWPSProcessExecution) runtimes.get(executionId);
								try {
									if (runtime != null) {
										if(runtime.getStatus() != null && 
											"RUNNING".equalsIgnoreCase(runtime.getStatus())) {
											runtime.updateRuntimeStatus();
										}
									} else {
										String owner = null;
										String name = null;
										String description = null;
										String statusLocation = null;
										String status = null;
										Date startDate = null;
										float progress = 0;
										runtime = extractRuntimeFromGeoStore(
												r, executionId, owner, name, description,
												statusLocation, status, startDate, progress);
										
										if (owner == null) {
											owner = extractOwner(secRules);
										}
										runtime.getDetails().put("owner", owner);
										
										runtimes.put(executionId, runtime);
									}

									runtime.getDetails().put("resourceId", r.getId());
									runtime.getDetails().put("resourceVisibility", extractVisibility(secRules));
									runtime.getDetails().put("message", "");
									
									runtime.getResults().put("mapId", 5800);
								} catch (Exception e) {
									LOGGER.log(Level.SEVERE, "[" + executionId + "] Could not update Runtime Status", e);
								}
									
								r.setLastUpdate(new Date());
								
								RESTResource resource = new RESTResource();
								resource.setName(executionId);
								resource.setMetadata(getServiceId());
								
								RESTStoredData store = new RESTStoredData(r.getData().getData());
								resource.setStore(store);
								
								RESTCategory geoStoreCategory = new RESTCategory("WPS_RUN_CONFIGS");
								resource.setCategory(geoStoreCategory);
								
								final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
								sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
								
								List<ShortAttribute> attributes = new ArrayList<ShortAttribute>();
								attributes.add(new ShortAttribute("name", runtime.getName(), DataType.STRING));
								attributes.add(new ShortAttribute("description", runtime.getDescription(), DataType.STRING));
								attributes.add(new ShortAttribute("statusLocation", (runtime.getStatusLocation() != null ? runtime.getStatusLocation() : ""), DataType.STRING));
								attributes.add(new ShortAttribute("status", runtime.getStatus(), DataType.STRING));
								attributes.add(new ShortAttribute("progress", String.valueOf(runtime.getProgress()), DataType.STRING));
								
								if (runtime.getStartDate() != null) {
									attributes.add(new ShortAttribute("startDate", sdf.format(runtime.getStartDate()), DataType.STRING));
								}
								
								if (runtime.getEndDate() != null) {
									attributes.add(new ShortAttribute("endDate", sdf.format(runtime.getEndDate()), DataType.STRING));
								}

								resource.setAttribute(attributes);
								
								wpsRestAPIGeoStoreAdminClient.updateResource(r.getId(), resource);
							}
						} else {
							runtime = (RestWPSProcessExecution) runtimes.get(executionId);
							if (runtime != null) {
								runtimes.remove(executionId);
							}
						}
					}
				}
			}
			
			return restServiceRuntime;
		}
	}

	/**
	 * 
	 * @param secRules
	 * @return
	 */
	protected static RESTResourceVisibility extractVisibility(SecurityRuleList secRules) {
		
		RESTResourceVisibility visibility = null;
		
		if (secRules != null) {
			visibility = RESTResourceVisibility.PRIVATE;
			
			for (RESTSecurityRule security : secRules.getList()) {
				if ( security.getGroup() != null && security.isCanRead()) {
					if(visibility != RESTResourceVisibility.PUBLIC) {
						visibility = RESTResourceVisibility.SHARED;
					} 
					
					if ("everyone".equalsIgnoreCase(security.getGroup().getGroupName())) {
						visibility = RESTResourceVisibility.PUBLIC;
						break;
					}
				}
			}
		}
		
		return visibility;
	}
	
	/**
	 * 
	 * @param secRules
	 * @return
	 */
	protected static String extractOwner(SecurityRuleList secRules) {
		for (RESTSecurityRule security : secRules.getList()) {
			if ( security.getUser() != null) {
				return security.getUser().getName();
			}
		}
		
		return null;
	}

	/**
	 * @param auth
	 * @param secRules
	 * @param allowed
	 * @return
	 */
	protected static boolean isAllowed(Principal auth, SecurityRuleList secRules) {
		
		boolean allowed = false;
		
		// secured resource
		for (RESTSecurityRule security : secRules.getList()) {
			if ( security.getUser() != null && 
					security.getUser().getName().equals(auth.getName()) && 
					(security.isCanRead() || security.isCanWrite() ) ) {
				allowed = true;
				break;
			}
			else if (security.getGroup() != null) {
				final String groupName = security.getGroup().getGroupName();
				if ("everyone".equalsIgnoreCase(groupName)) {
					allowed = true;
					break;
				} else if (auth instanceof UsernamePasswordAuthenticationToken) {
					UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) auth;
					for (GrantedAuthority userGroupAndrole : authToken.getAuthorities()) {
						if (userGroupAndrole.getAuthority().equals(groupName)) {
							allowed = true;
							break;
						}
					}
				}
			}
		}
		
		return allowed;
	}

	/**
	 * @param r
	 * @param executionId
	 * @param name
	 * @param description
	 * @param statusLocation
	 * @param status
	 * @param startDate
	 * @param progress
	 * @return
	 * @throws NumberFormatException
	 */
	protected RestWPSProcessExecution extractRuntimeFromGeoStore(Resource r,
			final String executionId, String owner, String name, String description,
			String statusLocation, String status, Date startDate, float progress)
			throws NumberFormatException {
		for ( Attribute a : r.getAttribute() )
		{
			if ( "owner".equals(a.getName()) ) {
				owner = a.getValue();
			}

			if ( "name".equals(a.getName()) ) {
				name = a.getValue();
			}

			if ( "description".equals(a.getName()) ) {
				description = a.getValue();
			}

			if ( "statusLocation".equals(a.getName()) ) {
				statusLocation = a.getValue();
			}
			
			if ( "status".equals(a.getName()) ) {
				status = a.getValue();
			}
			
			if ( "progress".equals(a.getName()) ) {
				progress = Float.parseFloat(a.getValue());
			}

			if ( "startDate".equals(a.getName()) ) {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

				try {
					startDate = sdf.parse(a.getValue());
				} catch (ParseException e) {
					LOGGER.log(Level.WARNING, "Could not parse Runtime StartDate [" + a.getValue() + "]", e);
				}
			}
		}
		final RestWPSProcessExecution runtime = new RestWPSProcessExecution(executionId, name, description, wps, statusLocation, status, progress, startDate);
		return runtime;
	}
	
	/**
	 * @param exeRequest
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	protected ExecuteProcessResponse issueWPSAsyncRequest(
			ExecuteProcessRequest exeRequest) throws IOException,
			ServiceException {
		// create the response type
		ResponseDocumentType doc = wps.createResponseDocumentType(false, true, true, "result");
		DocumentOutputDefinitionType odt = (DocumentOutputDefinitionType) doc.getOutput().get(0);
		odt.setMimeType("application/json");
		odt.setAsReference(true);
		ResponseFormType responseForm = wps.createResponseForm(doc, null);
		exeRequest.setResponseForm(responseForm);

		// send the request
		ExecuteProcessResponse response = wps.issueRequest(exeRequest);
		return response;
	}
	
	/**
	 * Persist the process input parameters into GeoStore
	 * 
	 * @param auth
	 * @param requestBody
	 * @param name
	 * @param description
	 * @param executeResponse
	 * @return 
	 * @throws IOException
	 */
	protected Long persistToGeoStore(Principal auth, String requestBody,
			String name, String description, ExecuteResponseType executeResponse)
			throws IOException {
		Properties props = new Properties();
		final String statusLocation = new String(executeResponse.getStatusLocation());
		final String[] queryString = statusLocation.split("\\?");
		props.load(new StringReader(queryString[1].replaceAll("&", "\n")));
		
		final String executionId = (String) props.get("executionId");
		
		final RestWPSProcessExecution runtime = 
				new RestWPSProcessExecution(executionId, name, description, wps, statusLocation, null, 0, null);
		runtimes.put(executionId, runtime);

		RESTResource resource = new RESTResource();
		resource.setName(executionId);
		resource.setMetadata(getServiceId());
		
		RESTStoredData store = new RESTStoredData(requestBody);
		resource.setStore(store);
		
		RESTCategory geoStoreCategory = new RESTCategory("WPS_RUN_CONFIGS");
		resource.setCategory(geoStoreCategory);

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		List<ShortAttribute> attributes = new ArrayList<ShortAttribute>();
		attributes.add(new ShortAttribute("name", name, DataType.STRING));
		attributes.add(new ShortAttribute("description", description, DataType.STRING));
		attributes.add(new ShortAttribute("statusLocation", statusLocation, DataType.STRING));
		attributes.add(new ShortAttribute("status", runtime.getStatus(), DataType.STRING));
		attributes.add(new ShortAttribute("progress", String.valueOf(runtime.getProgress()), DataType.STRING));
		attributes.add(new ShortAttribute("owner", auth.getName(), DataType.STRING));
		attributes.add(new ShortAttribute("message", "", DataType.STRING));

		if (runtime.getStartDate() != null) {
			attributes.add(new ShortAttribute("startDate", sdf.format(runtime.getStartDate()), DataType.STRING));
		}
		
		if (runtime.getEndDate() != null) {
			attributes.add(new ShortAttribute("endDate", sdf.format(runtime.getEndDate()), DataType.STRING));
		}

		resource.setAttribute(attributes);
		
		Long resourceId = wpsRestAPIGeoStoreAdminClient.insert(resource);
		
		List<SecurityRule> list = new ArrayList<SecurityRule>();
		SecurityRule security = new SecurityRule();
		security.setResource(wpsRestAPIGeoStoreAdminClient.getResource(resourceId));
		security.setUser(wpsRestAPIGeoStoreAdminClient.getUser(auth.getName()));
		security.setCanRead(true);
		list.add(security);
		SecurityRuleList rules = new SecurityRuleList(list);
		wpsRestAPIGeoStoreAdminClient.updateSecurityRules(resourceId, rules);
		
		runtime.getDetails().put("owner", auth.getName());
		runtime.getDetails().put("message", "");
		runtime.getDetails().put("resourceId", resourceId);
		
		return resourceId;
	}

	/**
	 * @return the reasourceLoaderProcessIdent
	 */
	public String getReasourceLoaderProcessIdent() {
		return reasourceLoaderProcessIdent;
	}

	/**
	 * @param reasourceLoaderProcessIdent the reasourceLoaderProcessIdent to set
	 */
	public void setReasourceLoaderProcessIdent(
			String reasourceLoaderProcessIdent) {
		this.reasourceLoaderProcessIdent = reasourceLoaderProcessIdent;
	}
}
