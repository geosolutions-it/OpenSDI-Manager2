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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action handler for a Service box action
 * 
 * @author adiaz
 * 
 */
public class ServiceBoxActionHandlerImpl implements ServiceBoxActionHandler {

	private List<Callback> callbacks;

	/**
	 * Handle a GET request
	 * 
	 * @param request
	 * @param response
	 * 
	 * @return ServiceBoxActionParameters
	 * 
	 * @throws IOException
	 */
	public ServiceBoxActionParameters doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServiceBoxActionParameters callbackResult = null;
		if (callbacks != null) {
			for (Callback callback : callbacks) {
				callbackResult = callback.onGet(request, response,
						callbackResult);
				if (!callbackResult.isSuccess()) {
					// not success: return false
					break;
				}
			}
		}
		return callbackResult;
	}

	/**
	 * Handle a POST request
	 * 
	 * @param request
	 * @param responsetrue
	 *            if the request can continue or false otherwise
	 * 
	 * @return ServiceBoxActionParameters
	 * 
	 * @throws IOException
	 */
	public ServiceBoxActionParameters doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServiceBoxActionParameters callbackResult = null;
		if (callbacks != null) {
			for (Callback callback : callbacks) {
				callbackResult = callback.onPost(request, response,
						callbackResult);
				if (!callbackResult.isSuccess()) {
					// not success: return false
					break;
				}
			}
		}
		return callbackResult;
	}

	/**
	 * @return the callbacks
	 */
	public List<Callback> getCallbacks() {
		return callbacks;
	}

	/**
	 * @param callbacks
	 *            the callbacks to set
	 */
	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks = callbacks;
	}

}
