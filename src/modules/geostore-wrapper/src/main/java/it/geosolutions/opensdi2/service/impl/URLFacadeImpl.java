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
	 * Proxy service to be used
	 */
	protected ProxyService proxy;

	/**
	 * Url wrapped in this implementation of the URL Facade
	 */
	String urlWrapped;

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
			HttpServletResponse response, String... subPath) throws IOException {

		// prepare final url and put in the request
		String finalURL = this.getUrlWrapped();
		if (subPath != null) {
			for (String path : subPath) {
				finalURL += "/" + path;
			}
		}
		request.setAttribute(URL_NAME, finalURL);

		try {
			proxy.execute(request, response);
		} catch (ServletException e) {
			throw new IOException(e);
		}
	}

	/**
	 * @return the urlWrapped
	 */
	public String getUrlWrapped() {
		return urlWrapped;
	}

	/**
	 * @param urlWrapped
	 *            the urlWrapped to set
	 */
	public void setUrlWrapped(String urlWrapped) {
		this.urlWrapped = urlWrapped;
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

}
