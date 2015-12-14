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
package it.geosolutions.opensdi2.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;

import it.geosolutions.geostore.core.model.User;

/**
 * User interceptor interface
 * 
 * @author adiaz
 */
public interface UserInterceptorService {

    /**
     * Method to be called on user creation interception
     * 
     * @param user created
     */
    public void onUserCreation(User user);

    /**
     * Method to be called on user update
     * 
     * @param user updated
     */
    public void onUserUpdate(User user);

    /**
     * Method to be called on user delete
     * 
     * @param userId identifier of the user to be deleted
     */
    public void onUserDelete(Long userId);

    /**
     * Execute an user operation
     * 
     * @param operation
     * @param request
     * @param response
     * @return true if the request can continue and false otherwise
     */
    public boolean onUserOperation(String operation, HttpServletRequest request,
            HttpServletResponse response);

    /**
     * Method to be called when the callback has been finished
     */
    public void onFinish();

    /**
     * Method to be called on remote response
     * 
     * @param method
     * @throws IOException
     */
    public void onRemoteResponse(HttpMethod method) throws IOException;

    /**
     * Get credentials for the remote operation
     * 
     * @return credentials to perform the operation
     */
    WrappedCredentials getCredentials();
}
