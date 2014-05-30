/*
 *  ServiceBox
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
package it.geosolutions.servicebox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class for Service Box actions
 * 
 * @author adiaz
 */
public abstract class ServiceBoxActionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8403642389129639997L;

	private final static String PROPERTY_FILE_PARAM = "app.properties";
	private final static Logger LOGGER = Logger
			.getLogger(ServiceBoxActionServlet.class.getSimpleName());

	/**
	 * Service box properties
	 */
	protected Properties properties = new Properties();

	/**
	 * Default action handler name. Used to read from the spring context when
	 * another one is defined
	 */
	protected static final String DEFAULT_ACTION_HANDLER = "serviceBoxActionHandler";

	/**
	 * Temporal folder for the file upload
	 */
	protected static final String DEFAULT_TMP_FOLDER = "temp";

	/**
	 * Service box action handler for this action.
	 */
	protected ServiceBoxActionHandler serviceBoxActionHandler;

	/**
	 * Name of this action. You must change this name on each action before on
	 * init method. It's used to save specific configuration by action. If you
	 * don't change it, it use the simple class name of the runtime servlet
	 */
	protected String actionName;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceBoxActionServlet() {
		super();
	}

	/**
	 * Init method of the servlet. Don't forgot override it on the actions and
	 * change the action name
	 */
	public void init(ServletConfig servletConfig) throws ServletException {
		this.actionName = this.getClass().getSimpleName();
		super.init(servletConfig);
		String appPropertyFile = getServletContext().getInitParameter(
				PROPERTY_FILE_PARAM);
		InputStream inputStream = ServiceBoxActionServlet.class
				.getResourceAsStream(appPropertyFile);
		try {
			properties.load(inputStream);
			ServletContext context = getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(context);
			String actionHandler = DEFAULT_ACTION_HANDLER;
			if (actionName != null
					&& properties != null
					&& properties.containsKey(actionName + "."
							+ DEFAULT_ACTION_HANDLER)) {
				actionHandler = properties.getProperty(actionName + "."
						+ DEFAULT_ACTION_HANDLER);
			}
			serviceBoxActionHandler = (ServiceBoxActionHandler) wac
					.getBean(actionHandler);
			initTemporalFolder();
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE,
						"Error encountered while processing properties file", e);
			}
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE,
							"Error building the action configuration ", e);
				throw new ServletException(e.getMessage());
			}
		}
	}

	/**
	 * Do GET envelope
	 */
	protected final void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServiceBoxActionParameters actionParameters = serviceBoxActionHandler
				.doGet(request, response);
		if (actionParameters.isSuccess()) {
			doGetAction(request, response, actionParameters);
		}
	}

	/**
	 * Do POST envelope
	 */
	protected final void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServiceBoxActionParameters actionParameters = serviceBoxActionHandler
				.doPost(request, response);
		if (actionParameters.isSuccess()) {
			doPostAction(request, response, actionParameters);
		}
	}

	/**
	 * Check if the temporal folder exists and create it
	 */
	private void initTemporalFolder() {
		String path = null;
		try {
			path = properties.getProperty(DEFAULT_TMP_FOLDER);
			File tempFolder = new File(path);
			if (!tempFolder.exists()) {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.info("Creating temporal folder: '" + path + "'");
				}
				tempFolder.mkdir();
			} else {
				if (LOGGER.isLoggable(Level.INFO)) {
					LOGGER.info("Temporal folder: '" + path
							+ "' already exists");
				}
			}
		} catch (Exception e) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				if (path != null) {
					LOGGER.info("Can't create temporal folder: '" + path + "'");
				} else {

					LOGGER.info("Can't create temporal folder");
				}
			}
		}
	}

	/**
	 * Do GET enveloped method
	 * 
	 * @param request
	 * @param response
	 * @param serviceBoxActionParameters
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract void doGetAction(HttpServletRequest request,
			HttpServletResponse response,
			ServiceBoxActionParameters serviceBoxActionParameters)
			throws ServletException, IOException;

	/**
	 * Do POST enveloped method
	 * 
	 * @param request
	 * @param response
	 * @param serviceBoxActionParameters
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract void doPostAction(HttpServletRequest request,
			HttpServletResponse response,
			ServiceBoxActionParameters serviceBoxActionParameters)
			throws ServletException, IOException;
}
