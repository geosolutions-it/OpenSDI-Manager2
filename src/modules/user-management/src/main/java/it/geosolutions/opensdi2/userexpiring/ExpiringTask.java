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
package it.geosolutions.opensdi2.userexpiring;

import freemarker.template.TemplateException;
import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserAttribute;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTUser;
import it.geosolutions.opensdi2.email.OpenSDIMailer;
import it.geosolutions.opensdi2.userexpiring.model.ExecutionState;
import it.geosolutions.opensdi2.userexpiring.model.ExpireLogElement;
import it.geosolutions.opensdi2.userexpiring.model.NotificationLogElement;
import it.geosolutions.opensdi2.userexpiring.model.TaskState;
import it.geosolutions.opensdi2.userexpiring.model.UserExpiringStatus;
import it.geosolutions.opensdi2.utils.email.EmailUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;

/**
 * Class that wraps the method execution for expiring users check. Send email if
 * configured to do it
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
public class ExpiringTask {
	private final static Logger LOGGER = Logger.getLogger(ExpiringTask.class);

	private UserExpiringStatus userExpiringTaskStatus;

	private AdministratorGeoStoreClient administratorGeoStoreClient;

	OpenSDIMailer notificationService;

	/**
	 * email subject
	 */
	private String userSubject;
	/**
	 * email subject
	 */
	private String checksumSubject;

	/**
	 * email from address (note: gmail or other services could not allow to use
	 * an address different from the account)
	 * 
	 */
	private String mailFrom;

	/**
	 * email address for notification
	 */
	private String notifyTo;

	/**
	 * Date Format for parsing
	 */
	private String dateFormat;

	/**
	 * Attribute name for expiring date
	 */
	private String expiringDateAttribute;
	/**
	 * email attribute
	 */
	private String emailAttribute;

	private boolean notifyUsers;

	private boolean autoExpireUsers;

	/**
	 * this method check if the program is configured to expire users and then
	 * run the process. Can be schedule
	 */
	public void execute() {
		if (autoExpireUsers) {
			LOGGER.info("User Expiring task started");
			runExpiringUsers();
			LOGGER.info("User Expiring task ended");
		}
	}

	/**
	 * This method check user expiring and notify the administrator
	 */
	public void runExpiringUsers() {
		Date executionStart = new Date();
		// check executing
		synchronized (userExpiringTaskStatus) {
			if (userExpiringTaskStatus.getStatus() == TaskState.Running) {
				userExpiringTaskStatus.setMessage("task was running at "
						+ new Date()
						+ " while trying to execute the process again");
				return;
			}// TODO syncronize on the status object

			// Log Start
			userExpiringTaskStatus.setStatus(TaskState.Running);
			userExpiringTaskStatus.setLastRun(executionStart.toString());
		}
		// check connection to geostore
		if (!pingGeoStore()) {

			synchronized (userExpiringTaskStatus) {
				userExpiringTaskStatus.setStatus(TaskState.Stopped);
				userExpiringTaskStatus
						.setLastExecutionOutCome((ExecutionState.Error));
				userExpiringTaskStatus.setMessage("GeoStore is unreachable at:"
						+ administratorGeoStoreClient.getGeostoreRestUrl());

			}

			return;
		} else if (userExpiringTaskStatus.getMessage() != null) {
			// Remove the message if GeoStore is reachable again
			// TODO improve this check
			if (userExpiringTaskStatus.getMessage().contains(
					"GeoStore is unreachable")) {
				userExpiringTaskStatus.setMessage(null);
			}
		}

		// Setup
		ExpiringUpdater updater = new ExpiringUpdater();
		updater.setClient(administratorGeoStoreClient);
		updater.setDateFormat(dateFormat);
		updater.setAttributeName(expiringDateAttribute);

		// instantiate log element
		ExpireLogElement e = null;
		List<User> expired = null;
		// run
		try {

			// Check users and expire accounts
			expired = updater.checkUserList();

			// add log entry to the execution log
			// only if some account expired
			boolean mailSuccess = true;
			if (expired != null && expired.size() > 0) {
				e = new ExpireLogElement();
				// set users in log
				e.setExpiredUsers(getRestUsers(expired));
				e.setDate(executionStart.toString());
				// send checksum
				if (notifyTo != null) {
					mailSuccess = notifyAdminUserExpiring(expired, e);
				}

				if (notifyUsers && emailAttribute != null) {
					if (notifyUsersAccountExpiring(expired, e) && mailSuccess) {
						e.setEmailNotificationStatusSummary(ExecutionState.Success);
					} else {
						e.setEmailNotificationStatusSummary(ExecutionState.Error);
						mailSuccess = false;
					}
				}
			}

			synchronized (userExpiringTaskStatus) {
				userExpiringTaskStatus
						.setLastExecutionOutCome((ExecutionState.Success));
				if (e != null) {
					e.setStatus(userExpiringTaskStatus
							.getLastExecutionOutCome());
				}
				userExpiringTaskStatus.setStatus(TaskState.Stopped);

			}
		} catch (Exception ee) {
			synchronized (userExpiringTaskStatus) {
				userExpiringTaskStatus.setStatus(TaskState.Stopped);
				userExpiringTaskStatus
						.setLastExecutionOutCome((ExecutionState.Error));
				userExpiringTaskStatus.setMessage(ee.getMessage());
				if (e != null) {
					e.setStatus(userExpiringTaskStatus
							.getLastExecutionOutCome());
				}
			}
			LOGGER.error("error during user expiring task execution", ee);

		} finally {
			// if the log element is initialized
			// an expiration occurred.
			userExpiringTaskStatus.setStatus(TaskState.Stopped);
			if (e != null) {
				userExpiringTaskStatus.addLog(e);
			}
		}

	}

	/**
	 * utility method for to get a rest user from a user TODO move it into a
	 * Utility class
	 */
	private List<RESTUser> getRestUsers(List<User> users) {
		List<RESTUser> restus = new ArrayList<RESTUser>();
		if (users == null) {
			return restus;
		}
		for (User u : users) {
			RESTUser ret = new RESTUser();
			ret.setName(u.getName());
			ret.setId(u.getId());
			ret.setRole(u.getRole());
			restus.add(ret);
		}
		return restus;

	}

	/**
	 * Notify expiring of the users
	 * 
	 * @param expired
	 *            the list of expired users
	 * @param logEl
	 *            the log element to fill
	 * @return false if some error occurred, true otherwise
	 */
	private boolean notifyAdminUserExpiring(List<User> expired,
			ExpireLogElement logEl) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("users", expired);
		NotificationLogElement notification = new NotificationLogElement();
		notification.setAddress(notifyTo);
		notification.setReceiver("*Administrator*");

		try {
			// create message
			String message = notificationService.applyTemplate(
					"adminExpireNotification.ftl", model);
			LOGGER.info("Sending email to:" + notifyTo);
			// sending email
			notificationService.sendMail(mailFrom, notifyTo, checksumSubject,
					message);
			LOGGER.info("Message sent to:" + notifyTo + ":\n" + message);
			notification.setStatus(ExecutionState.Success);
			logEl.getEmailNotificationLog().add(notification);
		} catch (IOException e) {
			// log error for template and return error message
			LOGGER.error("Error getting email template", e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error getting the email template");
			logEl.getEmailNotificationLog().add(notification);
			return false;
		} catch (TemplateException e) {
			// log error for template and return error message
			LOGGER.error("error during generation of email body", e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error during generation of email body");
			logEl.getEmailNotificationLog().add(notification);
			return false;
		} catch (MailException e) {
			// log error for email return error message
			LOGGER.error("error sending expire checksum email to" + notifyTo, e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error sending expire checksum email to "
					+ notifyTo);
			logEl.getEmailNotificationLog().add(notification);
			return false;
		}
		return true;

	}

	/**
	 * Notify expiring of the users. Users must include email as attribute
	 * 
	 * @param expired
	 *            the list of expired users
	 * @param logEl
	 *            the log element to fill
	 * @return false if some error occurred, true otherwise
	 */
	private boolean notifyUsersAccountExpiring(List<User> expired,
			ExpireLogElement logEl) {
		boolean ret = true;
		for (User user : expired) {
			if (!sendMailToUser(user, "userExpireNotification.ftl", logEl)) {
				ret = false;
			}
		}
		return ret;

	}

	/**
	 * Send email to the user. The user must have a field defined as email
	 * 
	 * @param user
	 *            the user to notify
	 * @param the
	 *            template to use
	 * @param logEl
	 *            the log element to fill
	 * @return false if some error occurred, true otherwise
	 */
	private boolean sendMailToUser(User user, String template,
			ExpireLogElement logEl) {
		Map<String, Object> model = new HashMap<String, Object>();
		NotificationLogElement notification = new NotificationLogElement();
		notification.setReceiver(user.getName());
		model.put("user", user);
		if (user.getAttribute() == null) {
			notification.setStatus(ExecutionState.Error);
			LOGGER.warn("Error getting user email for user: " + user.getName());
			notification.setMessage("Error getting user email");
			logEl.getEmailNotificationLog().add(notification);
		}
		String email = null;

		for (UserAttribute ua : user.getAttribute()) {
			if (emailAttribute.equals(ua.getName()))
				email = ua.getValue();
		}

		if (email == null || "".equals(email)) {
			LOGGER.warn("Error getting user email for user: " + user.getName());
			notification.setMessage("Couldn't find user email");
			notification.setStatus(ExecutionState.Error);
			logEl.getEmailNotificationLog().add(notification);
			return false;
		}
		if (!EmailUtil.validateEmail(email)) {
			LOGGER.warn("The user email is not valid for user: "
					+ user.getName());
			notification.setMessage("the user email is not valid");
			notification.setStatus(ExecutionState.Error);
			logEl.getEmailNotificationLog().add(notification);
			return false;
		}
		;
		notification.setAddress(email);
		try {
			// create message
			String message = notificationService.applyTemplate(template, model);
			LOGGER.info("Sending email to:" + email);
			// sending email
			notificationService.sendMail(mailFrom, email, userSubject, message);
			LOGGER.info("Message sent to " + email + ":\n" + message);
			notification.setStatus(ExecutionState.Success);
			logEl.getEmailNotificationLog().add(notification);
		} catch (IOException e) {
			// log error for template and return error message
			LOGGER.error("Error getting email template", e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error getting email template");
			logEl.getEmailNotificationLog().add(notification);
			return false;
		} catch (TemplateException e) {
			// log error for template and return error message
			LOGGER.error("error during generation of email body", e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error getting email template");
			logEl.getEmailNotificationLog().add(notification);
			return false;
		} catch (MailException e) {
			// log error for email return error message
			LOGGER.error("error sending expire checksum email to" + notifyTo, e);
			notification.setStatus(ExecutionState.Error);
			notification.setMessage("Error sending email message");
			logEl.getEmailNotificationLog().add(notification);
			return false;
		}
		return true;
	}

	/**
	 * Check GeoStore Connection
	 * 
	 * @param client
	 * @return
	 */
	protected boolean pingGeoStore() {
		try {
			administratorGeoStoreClient.getCategories();
			return true;
		} catch (Exception ex) {

			// ... and now for an awful example of heuristic.....
			Throwable t = ex;
			while (t != null) {
				if (t instanceof ConnectException) {
					LOGGER.warn("Testing GeoStore is offline");
					return false;
				}
				t = t.getCause();
			}
			return false;
		}
	}

	// GETTERS AND SETTERS
	public UserExpiringStatus getUserExpiringTaskStatus() {
		return userExpiringTaskStatus;
	}

	public void setUserExpiringTaskStatus(
			UserExpiringStatus userExpiringTaskStatus) {
		this.userExpiringTaskStatus = userExpiringTaskStatus;
	}

	public AdministratorGeoStoreClient getAdministratorGeoStoreClient() {
		return administratorGeoStoreClient;
	}

	public void setAdministratorGeoStoreClient(
			AdministratorGeoStoreClient administratorGeoStoreClient) {
		this.administratorGeoStoreClient = administratorGeoStoreClient;
	}

	public OpenSDIMailer getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(OpenSDIMailer notificationService) {
		this.notificationService = notificationService;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getNotifyTo() {
		return notifyTo;
	}

	public void setNotifyTo(String notifyTo) {
		this.notifyTo = notifyTo;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getUserSubject() {
		return userSubject;
	}

	public void setUserSubject(String userSubject) {
		this.userSubject = userSubject;
	}

	public String getChecksumSubject() {
		return checksumSubject;
	}

	public void setChecksumSubject(String checksumSubject) {
		this.checksumSubject = checksumSubject;
	}

	public String getExpiringDateAttribute() {
		return expiringDateAttribute;
	}

	public void setExpiringDateAttribute(String expiringDateAttribute) {
		this.expiringDateAttribute = expiringDateAttribute;
	}

	public String getEmailAttribute() {
		return emailAttribute;
	}

	public void setEmailAttribute(String emailAttribute) {
		this.emailAttribute = emailAttribute;
	}

	public boolean isNotifyUsers() {
		return notifyUsers;
	}

	public void setNotifyUsers(boolean notifyUsers) {
		this.notifyUsers = notifyUsers;
	}

	public boolean isAutoExpireUsers() {
		return autoExpireUsers;
	}

	public void setAutoExpireUsers(boolean autoExpireUsers) {
		this.autoExpireUsers = autoExpireUsers;
	}

}
