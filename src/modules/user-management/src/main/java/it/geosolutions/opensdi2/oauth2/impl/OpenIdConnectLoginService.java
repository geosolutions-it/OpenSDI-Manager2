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
package it.geosolutions.opensdi2.oauth2.impl;

import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.oauth2.OAuth2LoginService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.OIDCAccessTokenResponse;

/**
 * OAuth 2.0 implementation using the OpenID - Connect protocol.
 *  
 * @author mauro.bartolomeoli@geo-solutions.it
 *
 */
public class OpenIdConnectLoginService implements OAuth2LoginService {
	
	@Override
	public void login(OSDIConfigurationKVP configuration, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		State state = new State();
        session.setAttribute("state", state.getValue());
        // store return page to use it when it will be callback time
        session.setAttribute("returnPage", request.getParameter("returnUrl"));
        ResponseType responseType = new ResponseType();
        responseType.add(ResponseType.Value.CODE);
        try {
	        String loginUrl = configuration.getValue("loginUrl", "").toString();
			String clientId = configuration.getValue("clientId", "").toString();
			String returnUrl = configuration.getValue("returnUrl", "").toString();
			String authorizations = configuration.getValue("authorizations", "openid email profile").toString();
			if(!loginUrl.isEmpty() && !clientId.isEmpty() && !returnUrl.isEmpty()) {
				AuthorizationRequest req = new AuthorizationRequest(
		                new URL(loginUrl),
		                responseType,
		                new ClientID(clientId),
		                new URL(returnUrl),
		                Scope.parse(authorizations),
		                state
		        );
	       
		        String location = req.toHTTPRequest().getURL().toExternalForm()+"?"+req.toHTTPRequest().getQuery();
		        // redirect to service login page
	            response.sendRedirect(location);
			} else {
				throw new IllegalArgumentException("Configuration is not valid. Either loginUrl, clientId or returnUrl are missing");
			}
        } catch (SerializeException e) {
            throw new IllegalArgumentException("Cannot redirect to OpenID Connect service.");
        } catch (IOException e) {
        	throw new IllegalArgumentException("Cannot redirect to OpenID Connect service.");
		}

	}
	
	@Override
	public AccessToken getToken(OSDIConfigurationKVP configuration, String code) {
		
		String clientId = configuration.getValue("clientId", "").toString();
		String clientSecret = configuration.getValue("clientSecret", "").toString();
		String returnUrl = configuration.getValue("returnUrl", "").toString();
		String tokenUrl = configuration.getValue("tokenUrl", "").toString();
		
		try {
			if(!tokenUrl.isEmpty() && !clientId.isEmpty() && !clientSecret.isEmpty() && !returnUrl.isEmpty()) {
				TokenRequest tokenRequest = new TokenRequest(new URL(tokenUrl),
						new ClientSecretPost(new ClientID(clientId), new Secret(
								clientSecret)), new AuthorizationCodeGrant(
								new AuthorizationCode(code), new URL(returnUrl)));
	
				HTTPResponse resp = tokenRequest.toHTTPRequest().send();
				OIDCAccessTokenResponse tokenResponse = OIDCAccessTokenResponse
						.parse(resp);
				return tokenResponse.getAccessToken();
			} else {
				throw new IllegalArgumentException("Configuration is not valid. Either tokenUrl, clientId, clientSecret or returnUrl are missing");
			}
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Malformed tokenUrl: " + tokenUrl);
		} catch (SerializeException e) {
			throw new IllegalArgumentException("Error getting token from OAuth2 service");
		} catch (IOException e) {
			throw new IllegalArgumentException("Error getting token from OAuth2 service");
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error parsing token from OAuth2 service");
		}
        
	}

	@Override
	public void returnToClient(OSDIConfigurationKVP configuration, HttpServletResponse response, HttpSession session, AccessToken token) {
		try {
			response.sendRedirect((String)session.getAttribute("returnPage") + "?token=" + token.toJSONString());
		} catch (IOException e) {
			throw new IllegalArgumentException("Error calling application page");
		}
		
	}

}
