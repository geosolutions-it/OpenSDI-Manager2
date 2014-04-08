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
package it.geosolutions.opensdi2.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Facade for a URL.
 * 
 * @author adiaz
 * 
 */
public interface URLFacade {

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
	void handleRequest(HttpServletRequest request,
			HttpServletResponse response, String urlWrapped, String... path) throws IOException;
	
	/**
	 * Check if an URL is a wrapped one
	 * 
	 * @param url relative URL to check
	 * 
	 * @return true if is a wrapped one or false otherwise
	 */
	boolean isWrappedURL(String url);

}
