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
import it.geosolutions.opensdi2.service.UserInterceptorService;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

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
}
