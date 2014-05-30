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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action callback interface
 * 
 * @author adiaz
 * 
 */
public interface Callback {

	/**
	 * Handle a GET request
	 * 
	 * @param request
	 * @param response
	 * @param callbackResult
	 *            last callback result
	 * 
	 * @return CallbackResult
	 * 
	 * @throws IOException
	 */
	ServiceBoxActionParameters onGet(HttpServletRequest request,
			HttpServletResponse response,
			ServiceBoxActionParameters callbackResult) throws IOException;

	/**
	 * Handle a POST request
	 * 
	 * @param request
	 * @param response
	 * @param callbackResult
	 *            last callback result
	 * 
	 * @return CallbackResult
	 * 
	 * @throws IOException
	 */
	ServiceBoxActionParameters onPost(HttpServletRequest request,
			HttpServletResponse response,
			ServiceBoxActionParameters callbackResult) throws IOException;

}
