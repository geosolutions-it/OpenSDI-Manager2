/*
 *  OpenSDI Manager
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
package it.geosolutions.opensdi2.userexpiring;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserAttribute;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTUser;
import it.geosolutions.geostore.services.rest.model.UserList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class test the expiring date and test if a user is expired if expired
 * the user is disabled.
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
public class ExpiringUpdater {

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		this.formatter = new SimpleDateFormat(dateFormat);
	}

	private final static Logger LOGGER = Logger
			.getLogger(ExpiringUpdater.class);
	
	@Autowired
	private AdministratorGeoStoreClient client;
	/**
	 * The attribute to parse as expiring date
	 */
	private String attributeName = "expires";
	/**
	 * The date format to parse expiring date
	 */
	private String dateFormat="MM/dd/yyyy";
	/**
	 * the formatter for date
	 */
	SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);// e.g. dd-MMM-yyyy

	/**
	 * @param fileUploadService
	 *            the fileUploadService to set
	 */
	@Autowired
	public void setFileUploadService(AdministratorGeoStoreClient client) {
		this.client = client;
	}

	/**
	 * Check user list and found candidate users for expiring.
	 * if expired, set user enabled to false.
	 * 
	 */
	public ArrayList<User> checkUserList() {
		LOGGER.debug("getting user list...");
		ArrayList<User> expired = new ArrayList<User>();
		
		UserList l = client.getUsers();
		if (l != null && l.getList() != null) {
			LOGGER.debug("found " + l.getList().size()
					+ " users on GeoStore");
			for (RESTUser ru : l.getList()) {
				//Get User Info
				User u = client.getUser(ru.getId(), true);
				//Check user expiring
				if(checkExpired(u)){
					if(u.isEnabled()){
						expireUser(u);
						u.setEnabled(false);
						expired.add(u);
					}
					
					
				};
				
			}
		}
		return expired;
	}

	/**
	 * disable user using GeoStore client.
	 * Query geostore and check expiring date for each user.
	 * Disable them if the date is past
	 * @param u the user to disable
	 */
	private void expireUser(User u) {
		//create a new REST user to avoid attribute override
		User uu = new User();
		uu.setId(u.getId());
		uu.setName(u.getName());
		uu.setEnabled(false);
		try{
			client.update(u.getId(), uu);
		}catch(RuntimeException e){
			//Log exception and throw again
			LOGGER.error("error during user expiring",e);
			throw e;
		}
		
	}

	/**
	 * Check if the user is expired.
	 * parsing the expire attribute
	 * @param u the user to check
	 */
	private boolean checkExpired(User u) {
		List<UserAttribute> al = u.getAttribute();
		if (al != null) {
			for (UserAttribute a : al) {
				if (attributeName.equals(a.getName())) {
					if(LOGGER.isInfoEnabled()){
						LOGGER.info("User:");
						LOGGER.info(u.getName());
						LOGGER.info("expiring date:");
						LOGGER.info(a.getValue());
					}
					try {

						Date date = formatter.parse(a.getValue());
						LOGGER.info("expiring date parsed as:" + date);
						if(date.before(new Date())){
							LOGGER.info("user is expired");
							return true;
						}else{
							LOGGER.info("user is not expired");
							return false;
						}

					} catch (ParseException e) {
						LOGGER.warn("the date is not well formatted. skipping");
					}
					
					
				}
			}
		}
		
		return false;
		
	}

	// GETTERS AND SETTERS
	/**
	 * the client
	 * 
	 * @return
	 */
	public AdministratorGeoStoreClient getClient() {
		return client;
	}

	/**
	 * 
	 * @param client
	 *            the client to set
	 */
	public void setClient(AdministratorGeoStoreClient client) {
		this.client = client;
	}
}
