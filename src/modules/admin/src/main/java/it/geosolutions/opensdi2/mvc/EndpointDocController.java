package it.geosolutions.opensdi2.mvc;

import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.service.ProxyService;
import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.crud.api.PagebleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.model.mapping.EndPointMapping;
import it.geosolutions.opensdi2.model.mapping.facade.ProxyConfigBean;
import it.geosolutions.opensdi2.service.impl.URLFacadeImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Controller
@RequestMapping("/OpenSDIInfo")
public class EndpointDocController implements
		PagebleEntityCRUDController<EndPointMapping> {
	private final RequestMappingHandlerMapping handlerMapping;

	@Autowired
	public EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}
	@Autowired
	private URLFacadeImpl urlFacade;

	public @ResponseBody
	List<EndPointMapping> show() {
		List<EndPointMapping> results = new ArrayList<EndPointMapping>();
		Map<RequestMappingInfo, HandlerMethod> mappings = this.handlerMapping
				.getHandlerMethods();
		for (RequestMappingInfo info : mappings.keySet()) {
			EndPointMapping result = new EndPointMapping();
			result.setMethod(mappings.get(info));
			result.setInfo(info);
			results.add(result);
		}
		return results;
	}

	@Override
	@RequestMapping(value = "endpointdoc", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<EndPointMapping> list(Integer start, Integer limit)
			throws RESTControllerException {
		CRUDResponseWrapper<EndPointMapping> response = new CRUDResponseWrapper<EndPointMapping>();
		List<EndPointMapping> results = this.show();
		response.setData(results);
		response.setCount(results.size());
		response.setTotalCount(results.size());
		return response;
	}
	
	@RequestMapping(value = "facade", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<Map<String,Object>> listfacade(Integer start, Integer limit)
			throws RESTControllerException {
		CRUDResponseWrapper<Map<String,Object>> response = new CRUDResponseWrapper<Map<String,Object>>();
		Map<String,ProxyService> results = urlFacade.getCustomizedProxies();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(); 
		for(String key : results.keySet()){
			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("path",key);
			obj.put("proxy",getProxyConfig(results.get(key).getProxyConfig()));
			obj.put("urlWrapped",urlFacade.getUrlsWrapped().get(key));
			list.add(obj);
		}
		response.setData(list);
		response.setCount(list.size());
		response.setTotalCount(list.size());
		return response;
	}

	private ProxyConfig getProxyConfig(ProxyConfig proxyConfig) {
		return new ProxyConfigBean(proxyConfig);
	}
	
	
}