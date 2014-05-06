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
import it.geosolutions.geostore.core.model.UserAttribute;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.service.UserInterceptorService;
import it.geosolutions.opensdi2.service.WrappedCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

/**
 * This user interceptor call customized scripts in the user management
 * interception
 * 
 * @author adiaz
 * 
 */
public class ScriptsUserInterceptorImpl implements UserInterceptorService {

	/**
	 * GeoStoreClient in the applicationContext
	 */
	AdministratorGeoStoreClient geoStoreClient;

	/**
	 * '=' String
	 */
	private static final String EQUALS = "=";

	/**
	 * User identifier parameter for the scripts
	 */
	private static final String USERNAME = "USERNAME";

	/**
	 * User name parameter for the scripts
	 */
	private static final String NAME = "NAME";

	/**
	 * User surname parameter for the scripts
	 */
	private static final String SURNAME = "SURNAME";

	/**
	 * User email parameter for the scripts
	 */
	private static final String EMAIL = "EMAIL";

	/**
	 * User password parameter for the scripts
	 */
	private static final String PASS = "PASS";

	/**
	 * Library path parameter
	 */
	private static final String LIB_PATH = "LIB_PATH";

	/**
	 * Path system parameter
	 */
	private static final String PATH = "PATH";

	/**
	 * Known extra attributes in user attributes for the scripts
	 */
	private static final List<String> USER_KNOWN_ATTRIBUTES;
	static {
		USER_KNOWN_ATTRIBUTES = new LinkedList<String>();
		USER_KNOWN_ATTRIBUTES.add(NAME);
		USER_KNOWN_ATTRIBUTES.add(SURNAME);
		USER_KNOWN_ATTRIBUTES.add(EMAIL);
	}

	/**
	 * Encoding for the password in the script. Default is "{SSHA}"
	 */
	private String passwordEncoding = "{SSHA}";

	private final static Logger LOGGER = Logger
			.getLogger(ScriptsUserInterceptorImpl.class);
	/**
	 * # Creating a new user - the script will create also the FTP folder and
	 * will restart the ProFTP Service USERNAME=THEUSERNAME NAME=THENAME
	 * SURNAME=THESURNAME EMAIL=THEMAIL
	 * PASS={SSHA}P7BcwmTJLUJQzXAjVJ4N3nTnY68CkY0m ./create_user.sh
	 */
	private String createUserScript = "sh create_user.sh";

	/**
	 * # The function updates the user entries. The USERNAME must exist on LDAP,
	 * the other properties can be customized but all specified # - the
	 * following examples updates the User password - WARNING: this function
	 * does not affect the LDAP groups at all USERNAME=THEUSERNAME NAME=THENAME
	 * SURNAME=THESURNAME EMAIL=THEMAIL
	 * PASS={SSHA}L6lReG1EUTC0g8Ps9HgJGDc4bMttlA/9 modify_user.sh
	 */
	private String updateUserScript = "sh modify_user.sh";

	/**
	 * # The function removes the user from LDAP people and deletes ALL the user
	 * folder contents - WARNING: this function does not cleanup the LDAP groups
	 * USERNAME=THEUSERNAME remove_user.sh
	 */
	private String deleteUserScript = "sh remove_user.sh";

	/**
	 * Path that contains the scripts
	 */
	private String libPath = "/opt/ldap_manager/scripts";

	/**
	 * Libraries path used inside the script. You can use
	 * <code>echo $PATH</code> executed in bash
	 */
	private String systemPath = "/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.opensdi2.service.UserInterceptorService#onUserCreation
	 * (it.geosolutions.geostore.core.model.User)
	 */
	@Override
	public void onUserCreation(User user) {
		try {
			callScript(getUserInformation(user, true, true), createUserScript);
		} catch (Exception e) {
			LOGGER.error("Error calling to " + createUserScript, e);
		}
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
		try {
			callScript(getUserInformation(user, true, true), updateUserScript);
		} catch (Exception e) {
			LOGGER.error("Error calling to " + updateUserScript, e);
		}
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
			callScript(getUserInformation(user, false, false), deleteUserScript);
		} catch (Exception e) {
			LOGGER.error("Error calling to " + deleteUserScript, e);
		}
	}

	/**
	 * Get user information in a map
	 * 
	 * @param user
	 * @param copyPassword
	 * @param copyKnownAttributes
	 * @return user information to call the scripts
	 */
	public Map<String, String> getUserInformation(User user,
			boolean copyPassword, boolean copyKnownAttributes) {
		Map<String, String> userInformation = new HashMap<String, String>();
		userInformation.put(USERNAME, user.getName());

		// default information
		userInformation.put("NAME", user.getName());
		userInformation.put("SURNAME", "THESURNAME");
		userInformation.put("EMAIL", "THEMAIL");

		if (copyPassword)
			userInformation.put(PASS, passwordEncoding
					+ (user.getNewPassword() != null ? user.getNewPassword()
							: user.getPassword()));
		if (copyKnownAttributes && user.getAttribute() != null) {
			for (UserAttribute ua : user.getAttribute()) {
				if (USER_KNOWN_ATTRIBUTES.contains(ua.getName().toUpperCase())
						&& ua.getValue() != null && !ua.getValue().equals("")) {
					userInformation.put(ua.getName().toUpperCase(),
							ua.getValue());
				}
			}
		}

		return userInformation;
	}

	/**
	 * Call bash script with parameters
	 * 
	 * @param parameters
	 *            to call the script
	 * @param scriptPath
	 *            absolute path to the script
	 * @return process
	 * @throws IOException
	 */
	public Process callScript(Map<String, String> parameters, String scriptPath)
			throws IOException {

		String[] env = new String[parameters != null ? parameters.size() + 2
				: 2];

		String envParams = "";
		int i = 0;
		env[i++] = LIB_PATH + EQUALS + libPath;
		envParams += env[i - 1] + " ";
		env[i++] = PATH + EQUALS + systemPath + ":$" + LIB_PATH;
		envParams += env[i - 1] + " ";
		for (String key : parameters.keySet()) {
			env[i++] = key + EQUALS + parameters.get(key);
			envParams += env[i - 1] + " ";
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Running script: '" + envParams + scriptPath + "'");
		}
		Process process = Runtime.getRuntime().exec(scriptPath, env);

		// DEBUG the process execution
		if (LOGGER.isDebugEnabled()) {
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) {
				LOGGER.debug(line);
			}
			LOGGER.debug("Script terminated!");

		}

		return process;
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
	 * @return the createUserScript
	 */
	public String getCreateUserScript() {
		return createUserScript;
	}

	/**
	 * @param createUserScript
	 *            the createUserScript to set
	 */
	public void setCreateUserScript(String createUserScript) {
		this.createUserScript = createUserScript;
	}

	/**
	 * @return the updateUserScript
	 */
	public String getUpdateUserScript() {
		return updateUserScript;
	}

	/**
	 * @param updateUserScript
	 *            the updateUserScript to set
	 */
	public void setUpdateUserScript(String updateUserScript) {
		this.updateUserScript = updateUserScript;
	}

	/**
	 * @return the deleteUserScript
	 */
	public String getDeleteUserScript() {
		return deleteUserScript;
	}

	/**
	 * @param deleteUserScript
	 *            the deleteUserScript to set
	 */
	public void setDeleteUserScript(String deleteUserScript) {
		this.deleteUserScript = deleteUserScript;
	}

	/**
	 * @return the passwordEncoding
	 */
	public String getPasswordEncoding() {
		return passwordEncoding;
	}

	/**
	 * @param passwordEncoding
	 *            the passwordEncoding to set
	 */
	public void setPasswordEncoding(String passwordEncoding) {
		this.passwordEncoding = passwordEncoding;
	}

	/**
	 * @return the libPath
	 */
	public String getLibPath() {
		return libPath;
	}

	/**
	 * @param libPath
	 *            the libPath to set
	 */
	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}

	/**
	 * @return the systemPath
	 */
	public String getSystemPath() {
		return systemPath;
	}

	/**
	 * @param systemPath
	 *            the systemPath to set
	 */
	public void setSystemPath(String systemPath) {
		this.systemPath = systemPath;
	}

	@Override
	public void onFinish() {
		// nothing on complete
	}

	@Override
	public void onRemoteResponse(HttpMethod method) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onUserOperation(String operation,
			HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	/**
	 * Get credentials for the remote operation
	 * 
	 * @return credentials to perform the operation
	 */
	public WrappedCredentials getCredentials() {
		// Wrapped on user groups interceptor
		return null;
	}

}
