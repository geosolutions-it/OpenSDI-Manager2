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
package it.geosolutions.opensdi2.service.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.geostore.core.model.enums.Role;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.service.UserGroupService;
import it.geosolutions.opensdi2.service.UserInterceptorService;
import it.geosolutions.opensdi2.service.WrappedCredentials;

/**
 * This user interceptor call uses an user group service to synchronize user and groups on the user edition
 * 
 * @author adiaz
 * 
 */
public class UserGroupsInterceptorImpl implements UserInterceptorService {

    /**
     * GeoStoreClient in the applicationContext
     */
    AdministratorGeoStoreClient geoStoreClient;

    /**
     * Service to save the user group relations
     */
    UserGroupService userService;

    /**
     * User credentials to be saved
     */
    private Stack<User> usersToSave = new Stack<User>();

    /**
     * LDAP DN groups into users are ADMIN
     */
    private List<String> userAdminGroups;

    private final static Logger LOGGER = Logger.getLogger(UserGroupsInterceptorImpl.class);

    /**
     * Synchronize user groups when the request is finished (after other actions)
     */
    @Override
    public void onFinish() {
        if (!usersToSave.isEmpty())
            synchronizeUserGroups(usersToSave.pop());
    }

    /**
     * Synchronize user groups with the user information
     * 
     * @param user
     */
    private void synchronizeUserGroups(User user) {
        try {
            List<String> groups = new LinkedList<String>();
            for (UserGroup group : user.getGroups()) {
                groups.add(group.getGroupName());
            }
            if (!userService.setUserGroups(user.getName(), groups)) {
                LOGGER.error("Error synchronizing user groups for user " + user);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("User groups synchronized for user " + user);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error synchronizing user groups for user " + user, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.service.UserInterceptorService#onUserCreation (it.geosolutions.geostore.core.model.User)
     */
    @Override
    public void onUserCreation(User user) {
        usersToSave.add(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.service.UserIterceptorService#onUserUpdate(it .geosolutions.geostore.core.model.User)
     */
    @Override
    public void onUserUpdate(User user) {
        onUserCreation(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.service.UserIterceptorService#onUserDelete(java .lang.Long)
     */
    @Override
    public void onUserDelete(Long userId) {
        try {
            User user = geoStoreClient.getUser(userId);
            if (!userService.setUserGroups(user.getName(), null)) {
                LOGGER.error("Error synchronizing user groups for user " + user);
            } else {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("User groups synchronized for user " + user);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error synchronizing user groups for user " + userId, e);
        }
    }

    @Override
    public boolean onUserOperation(String operation, HttpServletRequest request,
            HttpServletResponse response) {

        boolean continueWithRequest = true;
        // this method cheat the ADMIN role for current LDAP admin groups,
        SecurityContext sc = SecurityContextHolder.getContext();
        String username = (String) sc.getAuthentication().getPrincipal();
        User user = geoStoreClient.getUser(username);

        List<String> groups = userService.getUserGroups(username);
        for (String group : groups) {
            if (userAdminGroups != null && userAdminGroups.contains(group)) {
                user.setRole(Role.ADMIN);
                break;
            }
        }

        if (operation != null) {
            if (operation.equals("details")) {
                try {
                    response.setContentType("application/json");

                    ObjectMapper mapper = new ObjectMapper();
                    AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
                    mapper.setAnnotationIntrospector(introspector);
                    mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
                    mapper.writeValue(response.getOutputStream(), user);
                    response.flushBuffer();
                    continueWithRequest = false;
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
        return continueWithRequest;
    }

    @Override
    public void onRemoteResponse(HttpMethod method) throws IOException {
    }

    /**
     * Get credentials for the remote operation
     * 
     * @return credentials to perform the operation
     */
    @Override
    public WrappedCredentials getCredentials() {
        // Get user groups from the secure context
        SecurityContext sc = SecurityContextHolder.getContext();
        String username = (String) sc.getAuthentication().getPrincipal();
        List<String> groups = userService.getUserGroups(username);

        // check if the group grant ADMIN role
        for (String group : groups) {
            if (userAdminGroups != null && userAdminGroups.contains(group)) {
                // return a wrapped credential based on the geostore client
                // credentials
                return new WrappedCredentials() {
                    @Override
                    public String getUserPassword() {
                        return geoStoreClient.getPassword();
                    }

                    @Override
                    public String getUserName() {
                        return geoStoreClient.getUsername();
                    }
                };
            }
        }

        // it isn't an admin: return null credentials
        return null;
    }

    /**
     * @return the geoStoreClient
     */
    public AdministratorGeoStoreClient getGeoStoreClient() {
        return geoStoreClient;
    }

    /**
     * @param geoStoreClient the geoStoreClient to set
     */
    public void setGeoStoreClient(AdministratorGeoStoreClient geoStoreClient) {
        this.geoStoreClient = geoStoreClient;
    }

    /**
     * @return the userService
     */
    public UserGroupService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserGroupService userService) {
        this.userService = userService;
    }

    /**
     * @return the usersToSave
     */
    public Stack<User> getUsersToSave() {
        return usersToSave;
    }

    /**
     * @param usersToSave the usersToSave to set
     */
    public void setUsersToSave(Stack<User> usersToSave) {
        this.usersToSave = usersToSave;
    }

    /**
     * @return the userAdminGroups
     */
    public List<String> getUserAdminGroups() {
        return userAdminGroups;
    }

    /**
     * @param userAdminGroups the userAdminGroups to set
     */
    public void setUserAdminGroups(List<String> userAdminGroups) {
        this.userAdminGroups = userAdminGroups;
    }
}
