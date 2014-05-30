/*
 *  Copyright (C) 2007 - 2014 GeoSolutions S.A.S.
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

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.utils.ProxyInfo;
import it.geosolutions.opensdi2.service.UserInterceptorService;
import it.geosolutions.opensdi2.service.WrappedCredentials;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXB;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * Create a new FTP user and his configuration when a new user is created in the
 * url.
 * 
 * @author adiaz
 */
public class UserCreationInterceptor extends AbstractProxyCallback implements
		ProxyCallback {

	private final static Logger LOGGER = Logger
			.getLogger(UserCreationInterceptor.class);

	/**
	 * User interceptors configured for this callback
	 */
	private List<UserInterceptorService> userInterceptors;

	/**
	 * Enable or disable this callback. By default is disabled
	 */
	private boolean enabled = false;

	/**
	 * REST user service URL to be intercepted
	 */
	private String interceptedURL = "";

	/**
	 * REST user service URLs to not be wrapped
	 */
	private List<String> notWrappedUrls;

	/**
	 * Default constructor
	 */
	public UserCreationInterceptor() {
		super();
	}

	/**
	 * @param config
	 */
	public UserCreationInterceptor(ProxyConfig config) {
		super(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.httpproxy.ProxyCallback#onRequest(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void onRequest(HttpServletRequest request,
			HttpServletResponse response, URL url) throws IOException {
		if (enabled) {
			try {
				// intercept user CRUD
				if (interceptedURL != null
						&& interceptedURL.equals(request.getAttribute("url"))
						&& "POST".equals(request.getMethod())) {
					// create user
					User user = (User) JAXB.unmarshal(request.getInputStream(),
							User.class);
					onUserCreation(user);
				} else if ("DELETE".equals(request.getMethod())
						&& ((String) request.getAttribute("url"))
								.startsWith(interceptedURL)) {
					// delete by id
					String userIdString = ((String) request.getAttribute("url"))
							.replace(interceptedURL + "user/", "").replaceAll(
									"/", "");
					Long userId = Long.decode(userIdString);
					onUserDelete(userId);
				} else if ("PUT".equals(request.getMethod())
						&& ((String) request.getAttribute("url"))
								.startsWith(interceptedURL)) {
					// update user
					User user = (User) JAXB.unmarshal(request.getInputStream(),
							User.class);
					onUserUpdate(user);
				}
			} catch (Exception e) {
				LOGGER.error("Error on user creation interceptor", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.httpproxy.ProxyCallback#onRemoteResponse(org.apache.commons
	 * .httpclient.HttpMethod)
	 */
	public void onRemoteResponse(HttpMethod method) throws IOException {
		// call on remote response
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Calling (remoteResponse): "
								+ interceptor.getClass().getSimpleName()
								+ " interceptor");
					}
					interceptor.onRemoteResponse(method);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user interception", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.httpproxy.ProxyCallback#onFinish()
	 */
	public void onFinish() throws IOException {
		// call on finish
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Calling (finish): "
								+ interceptor.getClass().getSimpleName()
								+ " interceptor");
					}
					interceptor.onFinish();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user interception", e);
		}
	}

	/**
	 * Call to user creation methods
	 * 
	 * @param user
	 */
	public void onUserCreation(User user) {
		if (LOGGER.isInfoEnabled())
			LOGGER.info("Create user " + user);
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Calling: "
								+ interceptor.getClass().getSimpleName()
								+ " interceptor");
					}
					interceptor.onUserCreation(user);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user creation interception", e);
		}
	}

	/**
	 * Call to user update methods
	 * 
	 * @param user
	 */
	public void onUserUpdate(User user) {
		if (LOGGER.isInfoEnabled())
			LOGGER.info("Update user " + user);
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Calling: "
								+ interceptor.getClass().getSimpleName()
								+ " interceptor");
					}
					interceptor.onUserUpdate(user);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user update interception", e);
		}
	}

	/**
	 * Call to user delete methods
	 * 
	 * @param userId
	 */
	public void onUserDelete(Long userId) {
		if (LOGGER.isInfoEnabled())
			LOGGER.info("delete user " + userId);
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					interceptor.onUserDelete(userId);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user update interception", e);
		}
	}

	/**
	 * Execute another operation
	 * 
	 * @param operation
	 * @param response
	 * @param request
	 */
	public boolean onUserOperation(String operation,
			HttpServletRequest request, HttpServletResponse response) {
		boolean continueWithRequest = true;
		if (LOGGER.isInfoEnabled())
			LOGGER.info("User operation: " + operation);
		try {
			if (userInterceptors != null) {
				for (UserInterceptorService interceptor : userInterceptors) {
					continueWithRequest = interceptor.onUserOperation(
							operation, request, response);
					// one user interceptor has flushed the response
					if (!continueWithRequest) {
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on user update interception", e);
		}
		return continueWithRequest;
	}

	/**
	 * @return the interceptedURL
	 */
	public String getInterceptedURL() {
		return interceptedURL;
	}

	/**
	 * @param interceptedURL
	 *            the interceptedURL to set
	 */
	public void setInterceptedURL(String interceptedURL) {
		this.interceptedURL = interceptedURL;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return the userInterceptors
	 */
	public List<UserInterceptorService> getUserInterceptors() {
		return userInterceptors;
	}

	/**
	 * @param userInterceptors
	 *            the userInterceptors to set
	 */
	public void setUserInterceptors(
			List<UserInterceptorService> userInterceptors) {
		this.userInterceptors = userInterceptors;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Callback method preExecuteProxyRequest executed before execute the proxy
	 * request
	 * 
	 * @param httpMethodProxyRequest
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param user
	 * @param password
	 * @param proxyInfo
	 * @return true if the proxy must continue and false otherwise
	 */
	public boolean beforeExecuteProxyRequest(HttpMethod httpMethodProxyRequest,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String user,
			String password, ProxyInfo proxyInfo) {
		// continue request or not
		boolean continueWithRequest = true;

		if (enabled) {
			// Get credentials from the interceptors
			WrappedCredentials credentials = null;
			for (UserInterceptorService interceptor : userInterceptors) {
				WrappedCredentials interceptorCredentials = interceptor
						.getCredentials();
				credentials = interceptorCredentials != null ? interceptorCredentials
						: credentials;
			}

			// here we return the user information wrapped
			if ("GET".equals(httpServletRequest.getMethod())
					&& ((String) httpServletRequest.getAttribute("url"))
							.startsWith(interceptedURL)) {
				// other operation
				String operation = ((String) httpServletRequest
						.getAttribute("url")).replace(interceptedURL + "user/",
						"").replaceAll("/", "");
				continueWithRequest = onUserOperation(operation,
						httpServletRequest, httpServletResponse);
			}

			// use user and password from credentials
			String userName = credentials != null ? credentials.getUserName()
					: user;
			String userPassword = credentials != null ? credentials
					.getUserPassword() : password;

			if (userName != null 
					&& userPassword != null
					&& mustUseWrappedCredentials((String) httpServletRequest.getAttribute("url"))) {
				// Basic authorization in the header with the new credentials
				httpMethodProxyRequest
						.removeRequestHeader(HttpHeaders.AUTHORIZATION);
				String encoding = new BASE64Encoder()
						.encode((userName + ":" + userPassword).getBytes());
				httpMethodProxyRequest.setRequestHeader(
						HttpHeaders.AUTHORIZATION, "Basic " + encoding);
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Continue: " + continueWithRequest);
			}
		}

		return continueWithRequest;
	}
	
	/**
	 * Check if the url must be wrapped or not. It allow wrap only some urls with ADMIN credentials
	 * 
	 * @param url to be checked
	 * 
	 * @return true if we need to use the wrapped credentials or false otherwise
	 */
	private boolean mustUseWrappedCredentials(String url){
		boolean mustUseWrappedCredentials = true;
		if(notWrappedUrls != null
				&& url != null){
			for(String notWrappedUrl: notWrappedUrls){
				if(url.startsWith(notWrappedUrl)){
					mustUseWrappedCredentials = false;
					break;
				}
			}
		}
		return mustUseWrappedCredentials;
	}

	/**
	 * @return the notWrappedUrls
	 */
	public List<String> getNotWrappedUrls() {
		return notWrappedUrls;
	}

	/**
	 * @param notWrappedUrls the notWrappedUrls to set
	 */
	public void setNotWrappedUrls(List<String> notWrappedUrls) {
		this.notWrappedUrls = notWrappedUrls;
	}
}
