/**
 * 
 */
package it.geosolutions.opensdi2.wps.rest.plugin.oaa;

import it.geosolutions.geostore.core.model.SecurityRule;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.dto.ShortAttribute;
import it.geosolutions.geostore.services.dto.search.BaseField;
import it.geosolutions.geostore.services.dto.search.FieldFilter;
import it.geosolutions.geostore.services.dto.search.SearchFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.RESTStoredData;
import it.geosolutions.geostore.services.rest.model.ResourceList;
import it.geosolutions.geostore.services.rest.model.SecurityRuleList;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;
import it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess;

import java.io.StringReader;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.Wps10Factory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alessio
 *
 */
public class RestWPSAssetAllocatorProcess extends RestWPSProcess {

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
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#getRuntimes()
	 */
	@Override
	public List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception {
		List<RestServiceRuntime> runtimes = new ArrayList<RestServiceRuntime>();
		
		SearchFilter searchFilter = new FieldFilter(BaseField.METADATA, getServiceId(), SearchOperator.EQUAL_TO);
		ResourceList resources = wpsRestAPIGeoStoreAdminClient.searchResources(searchFilter, -1, -1, true, true);
		
		return runtimes;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#execute(java.util.Map)
	 */
	@Override
	public String execute(Principal auth, String requestBody, Map<String, String> params) throws Exception {
		
		WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext())
        {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIden))
            {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found)
        {
            System.out.println("Skipping, gs:AreaGrid not found!");
            return null;
        }
        
        // based on the describeprocess, setup the execute
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIden);

        // add reference to the vectorial layer
        EObject vectorialLayerReference = Wps10Factory.eINSTANCE.createInputReferenceType();
        ((InputReferenceType)vectorialLayerReference).setMimeType("text/xml");
        ((InputReferenceType)vectorialLayerReference).setMethod(MethodType.POST_LITERAL);
        ((InputReferenceType)vectorialLayerReference).setHref("http://geoserver/wfs");
        
        ((InputReferenceType)vectorialLayerReference).setBody("<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\" xmlns:it.geosolutions=\"http://www.geo-solutions.it\"><wfs:Query typeName=\"it.geosolutions:states\"/></wfs:GetFeature>");
        
        exeRequest.addInput("features", Arrays.asList(vectorialLayerReference));

        // create the response type
        ResponseDocumentType doc = wps.createResponseDocumentType(false, true, true, "result");
        DocumentOutputDefinitionType odt = (DocumentOutputDefinitionType) doc.getOutput().get(0);
        odt.setMimeType("application/arcgrid");
        odt.setAsReference(true);
        ResponseFormType responseForm = wps.createResponseForm(doc, null);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.

        // we should get a raw response, no exception, no response document
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        
        //executeResponse.getStatusLocation().split("\\?")[1];
        Properties props = new Properties();
        final String statusLocation = new String(executeResponse.getStatusLocation());
		final String[] queryString = statusLocation.split("\\?");
		props.load(new StringReader(queryString[1].replaceAll("&", "\n")));
		
		final String executionId = (String) props.get("executionId");
        
		System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + executionId);
		
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
			    RESTResource resource = new RESTResource();
			    resource.setName(executionId);
			    resource.setMetadata(getServiceId());
			    
			    JsonFactory jsonF = new JsonFactory();
			    JsonParser jsonP = jsonF.createParser(requestBody);
			    JsonFactory f = new JsonFactory();
//			    2 JsonParser jp = f.createJsonParser(new File("user.json"));
//			    3 User user = new User();
//			    4 jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
//			    5 while (jp.nextToken() != JsonToken.END_OBJECT) {
//			    6   String fieldname = jp.getCurrentName();
//			    7   jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
//			    8   if ("name".equals(fieldname)) { // contains an object
			    
			    ObjectMapper mapper = new ObjectMapper();
			    mapper.readValue(userDataJSON, product.class);
			    
			    mapper.writeValueAsString(arg0);
			    
			    RESTStoredData store = new RESTStoredData(URLEncoder.encode(requestBody, "UTF-8"));
				resource.setStore(store);
				
				RESTCategory geoStoreCategory = new RESTCategory("WPS_RUN_CONFIGS");
				resource.setCategory(geoStoreCategory);

				List<ShortAttribute> attributes = new ArrayList<ShortAttribute>();
				attributes.add(new ShortAttribute("name1", "value3", DataType.STRING));
				attributes.add(new ShortAttribute("name2", "value2", DataType.STRING));
				attributes.add(new ShortAttribute("name3", "value1", DataType.STRING));
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
				
				//wpsRestAPIGeoStoreAdminClient.searchResources(searchFilter, page, entries, includeAttributes, includeData)
			}
		}
		
        return null;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#stop(it.geosolutions.opensdi2.rest.RestServiceRuntime, java.util.Map)
	 */
	@Override
	public String stop(RestServiceRuntime runtime, Map<String, String> params)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#supportsQueries()
	 */
	@Override
	public boolean supportsQueries() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#countRuntimes()
	 */
	@Override
	public int countRuntimes() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#findRuntimes(java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.util.Map, int, int)
	 */
	@Override
	public List<RestServiceRuntime> findRuntimes(String id, String status,
			Date startDate, Date endDate, Map<String, String> params, int page,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.wps.rest.plugin.RestWPSProcess#getRuntime(java.lang.String)
	 */
	@Override
	public RestServiceRuntime getRuntime(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
