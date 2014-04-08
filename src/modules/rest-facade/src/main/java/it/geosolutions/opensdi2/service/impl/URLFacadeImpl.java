/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.opensdi2.service.impl;

import it.geosolutions.httpproxy.service.ProxyService;
import it.geosolutions.opensdi2.service.URLFacade;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * URL Facade based on proxy service
 * 
 * @author adiaz
 * 
 */
public class URLFacadeImpl implements URLFacade {

	/**
	 * Urls wrapped in this implementation of the URL Facade (relative url as key and target url as value)
	 */
	private Map<String, String> urlsWrapped;

	/**
	 * Proxy service to be used
	 */
	protected ProxyService proxy;
	
	/**
	 * Custom proxies by url
	 */
	private Map<String, ProxyService> customizedProxies;

	/**
	 * Request parameter for the URL
	 */
	private static final String URL_NAME = "url";

	/**
	 * Handle a request in the URL wrapped
	 * 
	 * @param request
	 * @param response
	 * @param subPath
	 *            in the URL
	 * 
	 * @throws IOException
	 */
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response, String urlWrapped, String... subPath) throws IOException {
		
		if(isWrappedURL(urlWrapped)){
			// prepare final url and put in the request
			String finalURL = urlsWrapped.get(urlWrapped);
			if (subPath != null) {
				for (String path : subPath) {
					finalURL += "/" + path;
				}
			}
			request.setAttribute(URL_NAME, finalURL);

			try {
				// customized or default proxy
				if(customizedProxies != null && customizedProxies.containsKey(urlWrapped)){
					customizedProxies.get(urlWrapped).execute(request, response);
				}else{
					proxy.execute(request, response);
				}
			} catch (ServletException e) {
				throw new IOException(e);
			}
		}else{
			throw new IOException("Url not wrapped in this facade");
		}
		
	}

	/**
	 * @return the proxy
	 */
	public ProxyService getProxy() {
		return proxy;
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	public void setProxy(ProxyService proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the urlsWrapped
	 */
	public Map<String, String> getUrlsWrapped() {
		return urlsWrapped;
	}

	/**
	 * @param urlsWrapped the urlsWrapped to set
	 */
	public void setUrlsWrapped(Map<String, String> urlsWrapped) {
		this.urlsWrapped = urlsWrapped;
	}

	/**
	 * @return the customizedProxies
	 */
	public Map<String, ProxyService> getCustomizedProxies() {
		return customizedProxies;
	}

	/**
	 * @param customizedProxies the customizedProxies to set
	 */
	public void setCustomizedProxies(Map<String, ProxyService> customizedProxies) {
		this.customizedProxies = customizedProxies;
	}
	
	/**
	 * Check if an URL is a wrapped one
	 * 
	 * @param url relative URL to check
	 * 
	 * @return true if is a wrapped one or false otherwise
	 */
	public boolean isWrappedURL(String url) {
		return urlsWrapped != null && urlsWrapped.containsKey(url);
	}

}
