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
package it.geosolutions.opensdi2.servlet;

import it.geosolutions.opensdi2.service.URLFacade;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Rest facade. Delegated in url facade
 * 
 * @author Alejandro Diaz
 */
public class RestFacade extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7861833641555392499L;

    /**
     * URL Facade service
     */
    private URLFacade urlFacade;

    private static String PATH_SEPARATOR = "/";

    /**
     * Initialize the <code>ProxyServlet</code>
     * 
     * @param servletConfig The Servlet configuration passed in by the servlet conatiner
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        if (this.urlFacade == null) {
            ServletContext context = getServletContext();
            WebApplicationContext wac = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(context);
            urlFacade = (URLFacade) wac.getBean("urlFacade");
        }
    }

    /**
     * Perform the request based on this.urlFacade
     * 
     * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
     * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
     * @throws IOException if an error occur handling the request
     */
    public void handleRequest(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws IOException {

        String pathInfo = httpServletRequest.getPathInfo(), urlWrapped = null, path = "";

        for (String relPath : pathInfo.split(PATH_SEPARATOR)) {
            if (urlWrapped == null) {
                urlWrapped = StringUtils.isEmpty(relPath) ? null : relPath;
            } else {
                path += relPath + PATH_SEPARATOR;
            }
        }

        urlFacade.handleRequest(httpServletRequest, httpServletResponse, urlWrapped, path);
    }

    /**
     * Performs an HTTP GET request
     * 
     * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
     * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
     */
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException, ServletException {
        handleRequest(httpServletRequest, httpServletResponse);
    }

    /**
     * Performs an HTTP POST request
     * 
     * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
     * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
     */
    public void doPost(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws IOException, ServletException {
        handleRequest(httpServletRequest, httpServletResponse);
    }

    /**
     * Performs an HTTP PUT request
     * 
     * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
     * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
     */
    public void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException, ServletException {
        handleRequest(httpServletRequest, httpServletResponse);
    }

    /**
     * Performs an HTTP DELETE request
     * 
     * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
     * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
     */
    public void doDelete(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws IOException, ServletException {
        handleRequest(httpServletRequest, httpServletResponse);
    }

}
