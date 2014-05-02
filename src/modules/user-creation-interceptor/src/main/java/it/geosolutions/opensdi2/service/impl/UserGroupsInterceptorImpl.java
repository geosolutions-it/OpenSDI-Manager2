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

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.service.UserGroupService;
import it.geosolutions.opensdi2.service.UserInterceptorService;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * This user interceptor call uses an user group service to synchronize user and
 * groups on the user edition
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
	 * 
	 */
	private Stack<User> usersToSave = new Stack<User>();

	private final static Logger LOGGER = Logger
			.getLogger(UserGroupsInterceptorImpl.class);

	/**
	 * Synchronize user groups when the request is finished (after other actions) 
	 */
	public void onFinish() {
		synchronizeUserGroups(usersToSave.pop());
	}
	
	/**
	 * Synchronize user groups with the user information
	 * @param user
	 */
	private void synchronizeUserGroups(User user){
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
	 * @see
	 * it.geosolutions.opensdi2.service.UserInterceptorService#onUserCreation
	 * (it.geosolutions.geostore.core.model.User)
	 */
	@Override
	public void onUserCreation(User user) {
		usersToSave.add(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.opensdi2.service.UserIterceptorService#onUserUpdate(it
	 * .geosolutions.geostore.core.model.User)
	 */
	@Override
	public void onUserUpdate(User user) {
		onUserCreation(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.opensdi2.service.UserIterceptorService#onUserDelete(java
	 * .lang.Long)
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
			LOGGER.error("Error synchronizing user groups for user " + userId,
					e);
		}
	}

	/**
	 * @return the geoStoreClient
	 */
	public AdministratorGeoStoreClient getGeoStoreClient() {
		return geoStoreClient;
	}

	/**
	 * @param geoStoreClient
	 *            the geoStoreClient to set
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
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserGroupService userService) {
		this.userService = userService;
	}
}
