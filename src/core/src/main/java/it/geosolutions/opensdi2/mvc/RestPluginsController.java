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
import java.security.Principal;
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
	public @ResponseBody Object plugins() throws Exception
	{
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		List<RestService> data = new ArrayList<RestService>();
		RestAPIListDataWrapper<RestService> result = new RestAPIListDataWrapper<RestService>();
		result.set("type", "services");
		result.setCount(0);
		result.setTotalCount(0);

		int counter = 0, totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					totalCount = plugin.countServices();
					data = plugin.findServices(serviceId, name, activeStatus, params, page, pageSize);
				}
				// Sequential scan otherwise
				else {
					if (plugin.getServices() != null)
					_S: for (RestService service : plugin.getServices()) {
						totalCount = plugin.getServices().size();
						result.setTotalCount(totalCount);
						
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
							if (counter < (page*pageSize)) {
								counter++;
								continue _S;
							}
							if (counter > (page*pageSize)+pageSize-1) {
								break _S;
							}
							
						}
						
						data.add(service);
						counter++;
					}
				}
				
				//
				if (data.size() > 0) {
					result.setData(data);
					result.setCount(data.size());
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// the user principal
		final Principal auth = request.getUserPrincipal();
		
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				
				RestService service = null;
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					service = plugin.getService(serviceId);
					
					// Checking that the Service is available and correctly initialized
					if (service != null && !service.getActiveStatus().equals("ENABLED")) return null;
				}
				
				// Sequential scan otherwise
				if (service == null) {
					_S: for (RestService srv : plugin.getServices()) {
						
						// Checking that the Service is available and correctly initialized
						if (!srv.getActiveStatus().equals("ENABLED")) continue _S;
						
						// Filtering on plugin name
						if (serviceId != null && serviceId.length() > 0) {
							if (!srv.getServiceId().equals(serviceId)) continue _S;
						}
						
						service = srv;
						break;
					}
				}
				
				if (service != null) {
					if (request.getMethod().equals("GET")) {
						return service;
					}
					else if (request.getMethod().equals("POST")) {
						final String requestBody = extractParameters(params, request);
						return service.execute(auth, requestBody, params);
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * @param params
	 * @param request
	 * @return 
	 */
	private String extractParameters(Map<String, String> params,
			HttpServletRequest request) {
		try {						
			CharBuffer target = CharBuffer.allocate(4 * 1024 * 1024);
			request.getReader().read(target);
			
			if (target != null) {
				String requestBody = new String(target.array());
				
				// try to parse KvPs from the request body
				String[] kvps = requestBody.split("&");
				if (kvps != null && kvps.length > 0) {
					for (String kvp : kvps) {
						String[] paramKvP = kvp.split("=");
						if (paramKvP.length == 2) {
							params.put(paramKvP[0], paramKvP[1]);
						}
					}
				}
				
				return requestBody;
			}
		} catch (Exception cause) {
			LOGGER.log(Level.WARNING, "Exception occurred while reading the request content.", cause);
		}
		
		return null;
	}
	
/**
 *
	@RequestMapping(value = "/{pluginName}/services/{serviceId}/inputs", method = RequestMethod.GET)
	public @ResponseBody Object serviceInputs (
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		List<RestServiceRuntime> data = new ArrayList<RestServiceRuntime>();
		RestAPIListDataWrapper<RestServiceRuntime> result = new RestAPIListDataWrapper<RestServiceRuntime>();
		result.set("type", "inputs");
		result.setCount(0);
		result.setTotalCount(0);

		int totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				RestService service = null;
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					service = plugin.getService(serviceId);
				}
				
				// Sequential scan otherwise
				if (service == null) {
					_S: for (RestService srv : plugin.getServices()) {
						
						// Filtering on plugin name
						if (serviceId != null && serviceId.length() > 0) {
							if (!srv.getServiceId().equals(serviceId)) continue _S;
						}
						
						service = srv;
						break;
					}
				}
				
				if (service != null) {
					result.set("serviceId", serviceId);
					
					if(service.getInputs() != null) {
						data = service.getInputs();
						totalCount = data.size();
						result.setTotalCount(totalCount);
						result.setCount(totalCount);
					}
					
				}
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/{pluginName}/services/{serviceId}/outputs", method = RequestMethod.GET)
	public @ResponseBody Object serviceOutputs (
			@PathVariable String pluginName,
			@PathVariable String serviceId,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		List<RestServiceRuntime> data = new ArrayList<RestServiceRuntime>();
		RestAPIListDataWrapper<RestServiceRuntime> result = new RestAPIListDataWrapper<RestServiceRuntime>();
		result.set("type", "outputs");
		result.setCount(0);
		result.setTotalCount(0);

		int totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				RestService service = null;
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					service = plugin.getService(serviceId);
				}
				
				// Sequential scan otherwise
				if (service == null) {
					_S: for (RestService srv : plugin.getServices()) {
						
						// Filtering on plugin name
						if (serviceId != null && serviceId.length() > 0) {
							if (!srv.getServiceId().equals(serviceId)) continue _S;
						}
						
						service = srv;
						break;
					}
				}
				
				if (service != null) {
					result.set("serviceId", serviceId);
					
					if(service.getOutputs() != null) {
						data = service.getOutputs();
						totalCount = data.size();
						result.setTotalCount(totalCount);
						result.setCount(totalCount);
					}
					
				}
			}
		}
		
		return result;
	}
*
**/
	
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// the user principal
		final Principal auth = request.getUserPrincipal();

		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);
		
		List<RestServiceRuntime> data = new ArrayList<RestServiceRuntime>();
		RestAPIListDataWrapper<RestServiceRuntime> result = new RestAPIListDataWrapper<RestServiceRuntime>();
		result.set("type", "runtimes");
		result.setCount(0);
		result.setTotalCount(0);

		int counter = 0, totalCount = 0;
		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				
				RestService service = null;
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					service = plugin.getService(serviceId);
				}
				
				// Sequential scan otherwise
				if (service == null) {
					_S: for (RestService srv : plugin.getServices()) {
						
						// Filtering on plugin name
						if (serviceId != null && serviceId.length() > 0) {
							if (!srv.getServiceId().equals(serviceId)) continue _S;
						}
						
						service = srv;
						break;
					}
				}
				
				if (service != null) {
					result.set("serviceId", serviceId);

					// Try to use the datastore if available
					if (service.supportsQueries(auth)) {
						totalCount = service.countRuntimes(auth);
						data = service.findRuntimes(auth, id, status, startDate, endDate, params, page, pageSize);
					}
					// Sequential scan otherwise
					else {
						final List<RestServiceRuntime> runtimes = service.getRuntimes(auth);
						if(runtimes != null) {
							totalCount = runtimes.size();
							result.setTotalCount(totalCount);

							_R: for (RestServiceRuntime runtime : runtimes) {
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
										break _R;
									}
								}

								data.add(runtime);
								counter++;
							}
						}
					}
				}
			
				//
				if (data.size() > 0) {
					result.setData(data);
					result.setCount(data.size());
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
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		// the user principal
		final Principal auth = request.getUserPrincipal();

		List<RestPlugin> plugins = OpenSDIManagerConfigExtensions.extensions(RestPlugin.class);

		for (RestPlugin plugin : plugins) {
			if (plugin.getPluginName().equals(pluginName)) {
				
				RestService service = null;
				
				// Try to use the datastore if available
				if (plugin.supportsQueries()) {
					service = plugin.getService(serviceId);
				}
				
				// Sequential scan otherwise
				if (service == null) {
					_S: for (RestService srv : plugin.getServices()) {
						
						// Filtering on plugin name
						if (serviceId != null && serviceId.length() > 0) {
							if (!srv.getServiceId().equals(serviceId)) continue _S;
						}
						
						service = srv;
						break;
					}
				}
				
				if (service != null) {
					RestServiceRuntime runtime = null;

					// Try to use the datastore if available
					//if (service.supportsQueries(auth)) {
						runtime = service.getRuntime(auth, id);
					//}

					// Sequential scan otherwise
					if (runtime == null) {
						_R: for (RestServiceRuntime rt : service.getRuntimes(auth)) {
							// Filtering on runtime Id
							if (id != null && id.length() > 0) {
								if (!rt.getId().equals(id)) continue _R;
							}

							runtime = rt;
							break;
						}
					}

					//
					if (request.getMethod().equals("GET")) {
						return runtime;
					}
					else if (request.getMethod().equals("DELETE")) {
						extractParameters(params, request);
						return service.stop(auth, runtime, params);
					}
				}
			}
		}
		
		return null;
	}
	
}
