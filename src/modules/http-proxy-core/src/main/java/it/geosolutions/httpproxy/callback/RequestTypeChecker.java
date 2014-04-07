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
package it.geosolutions.httpproxy.callback;

import it.geosolutions.httpproxy.exception.HttpErrorException;
import it.geosolutions.httpproxy.service.ProxyConfig;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;

/**
 * RequestTypeChecker class for request type check.
 * 
 * @author Tobia Di Pisa at tobia.dipisa@geo-solutions.it
 * @author Alejandro Diaz
 */
public class RequestTypeChecker extends AbstractProxyCallback implements ProxyCallback {
	
	/**
	 * Compiled patterns
	 */
	private Set<Pattern> patterns;
    
	/**
	 * Last request types
	 */
    private Set<String> lastReqTypes;
	
    /**
     * Default constructor
     */
    public RequestTypeChecker() {
    	super();
    }

    /**
     * @param config
     */
    public RequestTypeChecker(ProxyConfig config) {
        super(config);
        loadPatterns();
    }
    
	/**
	 * Patterns set initializer
	 */
	private void loadPatterns() {
		Set<String> reqTypes = config.getReqtypeWhitelist();
		// Check if lastRequestTypes has changed
		if (hasChanged(reqTypes)) {
			patterns = new HashSet<Pattern>();
			if (reqTypes != null && reqTypes.size() > 0) {
				for (String regex : reqTypes) {
					patterns.add(Pattern.compile(regex));
				}
			}
		}
	}

	/**
	 * Auxiliary method to check if the reqType set has been changed
	 * 
	 * @param reqTypes
	 *            new set of request types
	 * 
	 * @return true if the request type parameters has been changed or false
	 *         otherwise
	 */
	private boolean hasChanged(Set<String> reqTypes) {

		boolean changed = false;

		if (lastReqTypes == null) {
			// Not compiled yet!
			changed = true;
		} else {

			for (String reqType : reqTypes) {
				if (!lastReqTypes.contains(reqType)) {
					// it's a new request type --> we must recompile
					changed = true;
					break;
				} else {
					lastReqTypes.remove(reqType);
				}
			}

			// if request types hasn't changed, the set must be empty
			changed = changed || !lastReqTypes.isEmpty();
		}

		// Save the new last request types and return the ''change'' value
		lastReqTypes = reqTypes;
		return changed;
	}

	/*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.httpproxy.ProxyCallback#onRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void onRequest(HttpServletRequest request, HttpServletResponse response, URL url)
            throws IOException {
    	loadPatterns();

        // //////////////////////////////////////
        // Check off the request type
        // provided vs. permitted request types
        // //////////////////////////////////////
    	
    	 String urlExtForm = url.toExternalForm();

         boolean check = false;
         for (Pattern pattern: patterns) {
             Matcher matcher = pattern.matcher(urlExtForm);

             if (matcher.matches()) {
                 check = true;
                 break;
             }
         }

         if (!check)
             throw new HttpErrorException(403, "Request Type"
                     + " is not among the ones allowed for this proxy");
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.httpproxy.ProxyCallback#onRemoteResponse(org.apache.commons.httpclient.HttpMethod)
     */
    public void onRemoteResponse(HttpMethod method) throws IOException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.httpproxy.ProxyCallback#onFinish()
     */
    public void onFinish() throws IOException {
    }

}
