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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.configurations.controller.OSDIModuleController;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.oauth2.OAuth2LoginService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nimbusds.oauth2.sdk.token.AccessToken;

/**
 * Login Service for OAuth2 compatible services.
 * 
 * @author Mauro Bartolomeoli
 *
 */
@Controller
@RequestMapping("/oauth")
public class OAuth2Controller extends OSDIModuleController {
	@Autowired
    @Resource(name="oauthService")
    OAuth2LoginService oAuthService;
	
	/**
	 * Single entry point for all login functionalities.
	 * 
	 * @param type login type (maps to a specific configuration file)
	 * @param request
	 * @param response
	 * @param session
	 * @throws OSDIConfigurationException
	 */
	@RequestMapping(value = "/login/{type}", method = RequestMethod.GET)
	public void login(@PathVariable String type, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws OSDIConfigurationException {
		// get configuration for the requested type
		OSDIConfigurationKVP configuration = (OSDIConfigurationKVP)loadConfiguration(request); 
		if(request.getParameter("code") == null || request.getParameter("code").trim().isEmpty()) {
			// 1st step: called by the app login page
			oAuthService.login(configuration, request, response, session);
		} else {
			// 2nd step: OAuth 2.0 service callback
			String state = request.getParameter("state");
			// check 
			if (session.getAttribute("state") == null || !state.equals(request.getSession().getAttribute("state"))) {
	            throw new IllegalArgumentException("Login failed");
			} else {
				// retrieve access token
				AccessToken token = oAuthService.getToken(configuration, request.getParameter("code"));
				if(token != null) {
					// redirect back to the client
					oAuthService.returnToClient(configuration, response, session, token);
				} else {
					throw new IllegalArgumentException("Login failed");
				}
			}
		}
		
	}

	@Override
	public String getInstanceID(HttpServletRequest req) {
		// instanceID is request {type} fragment (oauth/login/{type})
		return getPathFragment(req, 2);
	}
}
