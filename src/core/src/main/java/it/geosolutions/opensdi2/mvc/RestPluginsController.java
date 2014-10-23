/**
 * 
 */
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfigExtensions;
import it.geosolutions.opensdi2.rest.RestAPIBaseController;
import it.geosolutions.opensdi2.rest.RestAPIListDataWrapper;
import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestPlugin;
import it.geosolutions.opensdi2.rest.RestService;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	/**
	 * logger
	 */
	protected static final Logger LOGGER = Logger.getLogger("it.geosolutions.opensdi2.config");
	
	@RequestMapping(value = "/plugins", method = RequestMethod.GET)
	public @ResponseBody Object plugins() {
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		RestAPIListDataWrapper<RestPlugin> result = new RestAPIListDataWrapper<RestPlugin>();
		result.setData(plugins);
		result.set("type", "plugins");
		result.setCount(plugins.size());
		result.setTotalCount(plugins.size());

		return result;
	}

	@RequestMapping(value = "/{pluginName}/services", method = RequestMethod.GET)
	public @ResponseBody Object pluginServices(
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
		
		List<RestService> data = new ArrayList<RestService>();
		RestAPIListDataWrapper<RestService> result = new RestAPIListDataWrapper<RestService>();
		result.set("type", "services");
		result.setCount(0);
		result.setTotalCount(0);

		int counter = 0, servicesFound = 0, totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				if (plugin.getServices() != null)
				_S: for (RestService service : plugin.getServices()) {
					totalCount = plugin.getServices().size();
					result.setTotalCount(totalCount);
					
					// TODO: delegate the filtering and pagination to the plugin implementation
					
					// Filtering on activeStatus
					if (activeStatus != null && !activeStatus.equals("ALL")) {
						if (!service.getActiveStatus().equals(activeStatus)) continue _S;
					}

					// Filtering on plugin serviceId
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
						result.set("serviceId", serviceId);
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
						result.set("page", page);
						result.set("pageSize", pageSize);
						if (counter < (page*pageSize) || counter > (page*pageSize)+pageSize-1) {
							counter++;
							continue _S;
						}
					}
					
					data.add(service);
					servicesFound++;
					counter++;
				}
				if (servicesFound > 0) {
					result.setData(data);
					result.setCount(servicesFound);
				}
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/{pluginName}/services/{serviceId}", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody Object serviceDetails(
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			@RequestParam(required = false) Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) 
	{
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				_S: for (RestService service : plugin.getServices()) {
					
					// Filtering on plugin name
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
					}
					
					if (request.getMethod().equals("GET")) {
						return service;
					}
					else if (request.getMethod().equals("POST")) {
						try {						
							CharBuffer target = CharBuffer.allocate(1024);
							request.getReader().read(target);
							
							if (target != null) {
								String requestBody = new String(target.array());
								
								// try to parse KvPs from the request body
								String[] kvps = requestBody.split("&");
								if (kvps != null && kvps.length > 0) {
									for (String kvp : kvps) {
										String[] paramKvP = kvp.split("=");
										params.put(paramKvP[0], paramKvP[1]);
									}
								}
							}
						} catch (Exception cause) {
							LOGGER.log(Level.WARNING, "Exception occurred while reading the request content.", cause);
						}
						return service.execute(params);
					}
				}
			}
		}
		
		return null;
	}
	
	@RequestMapping(value = "/{pluginName}/services/{serviceId}/runtimes", method = RequestMethod.GET)
	public @ResponseBody Object serviceRuntimes (
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			@RequestParam(required = false, defaultValue = "ALL") String status,
			@RequestParam(required = false, defaultValue = "") String id,
			@RequestParam(required = false, defaultValue = "-1") int page,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate,
			@RequestParam(required = false) Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) 
	{
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		List<RestServiceRuntime> data = new ArrayList<RestServiceRuntime>();
		RestAPIListDataWrapper<RestServiceRuntime> result = new RestAPIListDataWrapper<RestServiceRuntime>();
		result.set("type", "runtimes");
		result.setCount(0);
		result.setTotalCount(0);

		int counter = 0, serviceRuntimesFound = 0, totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				_S: for (RestService service : plugin.getServices()) {
					
					// Filtering on plugin name
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
						result.set("serviceId", serviceId);
					}
					
					if (service.getRuntimes() != null) {
						totalCount = service.getRuntimes().size();
						result.setTotalCount(totalCount);

						_R: for (RestServiceRuntime runtime : service.getRuntimes()) {
							// TODO: delegate the filtering and pagination to the plugin implementation
							
							// Filtering on activeStatus
							if (status != null && !status.equals("ALL")) {
								if (!runtime.getStatus().equals(status)) continue _R;
							}

							// Filtering on runtime Id
							if (id != null && id.length() > 0) {
								if (!runtime.getId().equals(id)) continue _R;
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
									
									if (runtime.getParameters() == null || runtime.getParameters().isEmpty()) {
										continue _R;
									}
									else {
										boolean paramMatches = false;
										
										for (Entry<String, String> entry : filteringParameters.entrySet()) {
											final String key = entry.getKey();
											final String value = entry.getValue();
											
											for (RestItemParameter pp : runtime.getParameters()) {
												if (pp.getParamName().equalsIgnoreCase(key) && pp.getParamValue().equals(value)) {
													paramMatches = true;
													break;
												}
											}
										}
										
										if (!paramMatches) {
											continue _R;	
										}
									}
								}
							}

							// Pagination
							if (page >= 0) {
								result.set("page", page);
								result.set("pageSize", pageSize);
								if (counter < (page*pageSize)) {
									counter++;
									continue _R;
								}
								if (counter > (page*pageSize)+pageSize-1) {
									break _S;
								}
							}
							
							data.add(runtime);
							serviceRuntimesFound++;
							counter++;
						}
					}
				}
				if (serviceRuntimesFound > 0) {
					result.setData(data);
					result.setCount(serviceRuntimesFound);
				}
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/{pluginName}/services/{serviceId}/runtimes/{id}", method = {RequestMethod.GET, RequestMethod.DELETE})
	public @ResponseBody Object runtimeDetails(
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			@PathVariable String id,
			@RequestParam(required = false) Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) 
	{
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				_S: for (RestService service : plugin.getServices()) {
					
					// Filtering on plugin name
					if (serviceId != null && serviceId.length() > 0) {
						if (!service.getServiceId().equals(serviceId)) continue _S;
					}

					_R: for (RestServiceRuntime runtime : service.getRuntimes()) {
						// TODO: delegate the filtering and pagination to the plugin implementation
						
						// Filtering on runtime Id
						if (id != null && id.length() > 0) {
							if (!runtime.getId().equals(id)) continue _R;
						}

						if (request.getMethod().equals("GET")) {
							return runtime;
						}
						else if (request.getMethod().equals("DELETE")) {
							try {						
								CharBuffer target = CharBuffer.allocate(1024);
								request.getReader().read(target);
								
								if (target != null) {
									String requestBody = new String(target.array());
									
									// try to parse KvPs from the request body
									String[] kvps = requestBody.split("&");
									if (kvps != null && kvps.length > 0) {
										for (String kvp : kvps) {
											String[] paramKvP = kvp.split("=");
											params.put(paramKvP[0], paramKvP[1]);
										}
									}
								}
							} catch (Exception cause) {
								LOGGER.log(Level.WARNING, "Exception occurred while reading the request content.", cause);
							}
							return service.stop(runtime, params);
						}
					}
				}
			}
		}
		
		return null;
	}
	
}
