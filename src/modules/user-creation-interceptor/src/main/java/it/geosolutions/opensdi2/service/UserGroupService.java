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

import java.util.List;
import java.util.Map;

/**
 * User group service to manage user groups
 * 
 * @author adiaz
 */
public interface UserGroupService {

	/**
	 * Search an user by uid
	 * 
	 * @param uid
	 * 
	 * @return Map with the user attributes
	 */
	List<Map<String, Object>> search(String uid);

	/**
	 * Save user groups for an user
	 * 
	 * @param uid
	 * @param groups 
	 *  
	 * @return flag that indicates if the operation could be performed
	 */
	boolean setUserGroups(String uid, List<String> groups);
	
	/**
	 * Get groups for an user 
	 * 
	 * @param uid
	 * 
	 * @return groups that the user is member of
	 */
	List<String> getUserGroups(String uid);
}
