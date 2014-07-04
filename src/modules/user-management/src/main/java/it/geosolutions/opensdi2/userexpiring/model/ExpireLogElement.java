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
/**
 * 
 */
package it.geosolutions.opensdi2.userexpiring.model;

import it.geosolutions.geostore.services.rest.model.RESTUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for a log of operations
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
public class ExpireLogElement {
	/**
	 * The Execution date
	 */
	private String date;

	/**
	 * the user expired on that cycle
	 */
	private List<RESTUser> expiredUsers;
	/**
	 * Status
	 */
	private ExecutionState status;
	/**
	 * Email Notification Log
	 * 
	 */
	private List<NotificationLogElement> emailNotificationLog = new ArrayList<NotificationLogElement>();

	/**
	 * A summary status of email sending outcome
	 */
	private ExecutionState emailNotificationStatusSummary;

	// GETTERS AND SETTERS for all attrubutes above

	public ExecutionState getEmailNotificationStatusSummary() {
		return emailNotificationStatusSummary;
	}

	public void setEmailNotificationStatusSummary(
			ExecutionState emailNotificationStatusSummary) {
		this.emailNotificationStatusSummary = emailNotificationStatusSummary;
	}

	public List<NotificationLogElement> getEmailNotificationLog() {
		return emailNotificationLog;
	}

	public void setEmailNotificationLog(
			List<NotificationLogElement> emailNotificationLog) {
		this.emailNotificationLog = emailNotificationLog;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<RESTUser> getExpiredUsers() {
		return expiredUsers;
	}

	public void setExpiredUsers(List<RESTUser> expiredUsers) {
		this.expiredUsers = expiredUsers;
	}

	/**
	 * @return the status
	 */
	public ExecutionState getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ExecutionState status) {
		this.status = status;
	}
}
