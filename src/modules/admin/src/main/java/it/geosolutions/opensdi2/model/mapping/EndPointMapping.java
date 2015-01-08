package it.geosolutions.opensdi2.model.mapping;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class EndPointMapping {
	private HandlerMethod method;
	private RequestMappingInfo info;

	public HandlerMethod getMethod() {
		return method;
	}

	public void setMethod(HandlerMethod method) {
		this.method = method;
	}

	public RequestMappingInfo getInfo() {
		return info;
	}

	public void setInfo(RequestMappingInfo info) {
		this.info = info;
	}
	
}
