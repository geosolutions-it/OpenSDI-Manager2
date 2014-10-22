/**
 * 
 */
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfigExtensions;
import it.geosolutions.opensdi2.rest.RestAPIBaseController;
import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestPlugin;
import it.geosolutions.opensdi2.rest.RestService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author alessio.fabiani
 * 
 */
@Controller
public class RestPluginsController extends RestAPIBaseController {

	@RequestMapping(value = "/plugins", method = RequestMethod.GET)
	public @ResponseBody String plugins() {
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{\"plugins\":[");
		for (RestPlugin plugin : plugins) {
			sb.append(JSONize(plugin));
			sb.append(",");
		}
		if (plugins.size() > 0) sb.deleteCharAt(sb.length()-1);
		sb.append("]}");
		
		return sb.toString();
	}

	@RequestMapping(value = "/{pluginName}/services", method = RequestMethod.GET)
	public @ResponseBody String pluginServices(
			@PathVariable String pluginName,
			@RequestParam(required = false, defaultValue = "ALL") String activeStatus,
			@RequestParam(required = false, defaultValue = "") String serviceId,
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "-1") int page,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false) Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) 
	{
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		StringBuilder sb = new StringBuilder();
		sb.append("{\"services\":[");
		int counter = 0, servicesFound = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				_S: for (RestService service : plugin.getServices()) {
					
					// Filtering on activeStatus
					if (activeStatus != null && !activeStatus.equals("ALL")) {
						if (!service.getActiveStatus().equals(activeStatus)) continue _S;
					}

					// Filtering on plugin serviceId
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
					}

					// Filtering on plugin name
					if (name != null && name.length() > 0) {
						if (!service.getName().equalsIgnoreCase(name)) continue _S;
					}

					// Filtering on parameter value
					if (params != null && !params.isEmpty()) {
						Map<String, String> filteringParameters = new HashMap<String, String>();
						
						for (Entry<String, String> entry : params.entrySet()) {
							final String key = entry.getKey();
							final String value = entry.getValue();
							
							if (key.toLowerCase().startsWith("param_")) {
								final String paramName = key.toLowerCase().substring("param_".length());
								filteringParameters.put(paramName, value);
							}
						}
						
						if (!filteringParameters.isEmpty()) {
							
							if (service.getParameters() == null || service.getParameters().isEmpty()) {
								continue _S;
							}
							else {
								boolean paramMatches = false;
								
								for (Entry<String, String> entry : filteringParameters.entrySet()) {
									final String key = entry.getKey();
									final String value = entry.getValue();
									
									for (RestItemParameter pp : service.getParameters()) {
										if (pp.getParamName().equalsIgnoreCase(key) && pp.getParamValue().equals(value)) {
											paramMatches = true;
											break;
										}
									}
								}
								
								if (!paramMatches) {
									continue _S;	
								}
							}
						}
					}

					// Pagination
					if (page >= 0) {
						if (counter < (page*pageSize) || counter > (page*pageSize)+pageSize-1) {
							counter++;
							continue _S;
						}
					}
					
					sb.append(JSONize(service));
					sb.append(",");
					servicesFound++;
					counter++;
				}
				if (servicesFound > 0) sb.deleteCharAt(sb.length()-1);
			}
		}
		sb.append("]}");
		
		return sb.toString();
	}
	
	@RequestMapping(value = "/{pluginName}/services/{serviceId}", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String serviceDetails(
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			@RequestParam(required = false) Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) 
	{
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		StringBuilder sb = new StringBuilder();
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				_S: for (RestService service : plugin.getServices()) {
					
					// Filtering on plugin name
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
					}
					
					if (request.getMethod().equals("GET")) {
						sb.append(JSONize(service));
						break;
					}
					else if (request.getMethod().equals("POST")) {
						sb.append(service.execute(params));
						break;
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	/** ******************************************
	 * Utility methods
	 * **************************************** **/
	
	/**
	 * 
	 * @param plugin
	 * @return
	 */
	private static String JSONize(RestPlugin plugin) {
		StringBuilder jsonized = new StringBuilder();
		
		jsonized.append("{")
			.append("\"").append("pluginName").append("\"").append(":").append("\"").append(plugin.getPluginName()).append("\"").append(",")
			.append("\"").append("description").append("\"").append(":").append("\"").append(plugin.getDescription()).append("\"").append(",")
			.append("\"").append("version").append("\"").append(":").append("\"").append(plugin.getVersion()).append("\"")
		.append("}");
		
		return jsonized.toString();
	}
	
	/**
	 * 
	 * @param plugin
	 * @return
	 */
	private static String JSONize(RestService service) {
		StringBuilder jsonized = new StringBuilder();

		jsonized.append("{")
			.append("\"").append("serviceId").append("\"").append(":").append("\"").append(service.getServiceId()).append("\"").append(",")
			.append("\"").append("name").append("\"").append(":").append("\"").append(service.getName()).append("\"").append(",")
			.append("\"").append("description").append("\"").append(":").append("\"").append(service.getDescriprion()).append("\"").append(",")
			.append("\"").append("version").append("\"").append(":").append("\"").append(service.getVersion()).append("\"").append(",")
			.append("\"").append("activeStatus").append("\"").append(":").append("\"").append(service.getActiveStatus()).append("\"").append(",")
			.append("\"").append("parameters").append("\"").append(":").append("[").append(JSONize(service.getParameters())).append("]")
		.append("}");
		
		return jsonized.toString();
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	private static String JSONize(List<RestItemParameter> parameters) {
		StringBuilder jsonized = new StringBuilder();
		
		if (parameters != null) {
			for (RestItemParameter param : parameters) {
				jsonized.append("{")
					.append("\"").append(param.getParamName()).append("\"").append(":").append("\"").append(param.getParamValue()).append("\"")
				.append("}").append(",");
			}
			
			if (jsonized.length() > 0) jsonized.deleteCharAt(jsonized.length()-1);
		}
		
		return jsonized.toString();
	}

}
