/**
 * 
 */
package it.geosolutions.opensdi2.config;

import it.geosolutions.opensdi2.utils.ControllerUtils;

import java.io.Serializable;

/**
 * @author alessio.fabiani
 * 
 */
public class FileManagerConfigImpl implements OpenSDIManagerConfig, FileManagerConfig, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572206453695983591L;

	private String baseFolder;

	private String scope;

	/**
	 * @return base folder for the application
	 */
	public String getBaseFolder() {
		return baseFolder;
	}

	/**
	 * Setter for the base folder
	 * 
	 * @param base
	 *            folder for the application
	 */
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
		if (this.baseFolder != null
				&& this.baseFolder.lastIndexOf(ControllerUtils.SEPARATOR) != this.baseFolder
						.length() - 1) {
			this.baseFolder += ControllerUtils.SEPARATOR;
		}
	}

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

}
