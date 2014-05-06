/*
 *  Copyright (C) 2007 - 2011 GeoSolutions S.A.S.
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
package it.geosolutions.httpproxy.callback;

import it.geosolutions.httpproxy.exception.HttpErrorException;
import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.utils.ProxyInfo;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;

/**
 * A pluggable callback that can perform checks or alter the request.
 * 
 * @author Andrea Aime - GeoSolutions
 * @author Alejandro Diaz
 */
public interface ProxyCallback {
	
	/**
	 * Configure a callback with a proxi configuration
	 * 
	 * @param config
	 */
	void setProxyConfig(ProxyConfig config);

    /**
     * First to be called, can be used to initialize the callback status and disallow certain requests by throwing an {@link HttpErrorException}
     * 
     * @throws IOException
     */
    void onRequest(HttpServletRequest request, HttpServletResponse response, URL url)
            throws IOException;

    /**
     * Second to be called, can be used to check the remote server response
     * 
     * @param method
     * @throws IOException
     */
    void onRemoteResponse(HttpMethod method) throws IOException;

    /**
     * Called when the request is fully proxied, can be used for cleanup actions
     * 
     * @throws IOException
     */
    void onFinish() throws IOException;
    
	/**
	 * Callback method beforeExecuteProxyRequest executed before execute the
	 * proxy request
	 * 
	 * @param httpMethodProxyRequest
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param user
	 * @param password
	 * @param proxyInfo
	 * @return true if the proxy must continue and false otherwise
	 */
	boolean beforeExecuteProxyRequest(HttpMethod httpMethodProxyRequest,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String user,
			String password, ProxyInfo proxyInfo);
}
