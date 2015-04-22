/*
 *  OpenSDI Manager
 *  Copyright (C) 2015 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.oauth2;

import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nimbusds.oauth2.sdk.token.AccessToken;

/**
 * Login Service implementing the OAuth 2.0 protocol.
 *  
 * @author mauro.bartolomeoli@geo-solutions.it
 *
 */
public interface OAuth2LoginService {
	/**
	 * Redirects to the login page for the service.
	 * 
	 * @param response
	 * @param session
	 */
	public void login(OSDIConfigurationKVP configuration,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session);

	/**
	 * Retrieves a token for the given authentication code.
	 * 
	 * @param code
	 */
	public AccessToken getToken(OSDIConfigurationKVP configuration, String code);

	/**
	 * Calls back the calling page with an authentication token.
	 *  
	 * @param token
	 */
	public void returnToClient(OSDIConfigurationKVP configuration,
			HttpServletResponse response, HttpSession session, AccessToken token);
}
