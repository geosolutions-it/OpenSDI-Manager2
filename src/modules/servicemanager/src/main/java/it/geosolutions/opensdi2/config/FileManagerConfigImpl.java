/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.config;

import it.geosolutions.opensdi2.utils.ControllerUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * FileManagerConfig based on two maps:
 * <ul> 
 * 	<li>permissionsByFolder: that contains permission by folder</li>
 * 	<li>permissionsByLevel: that contains permission by levels if no specific configuration found</li>
 * </ul>
 * 
 * @author adiaz
 * 
 */
public class FileManagerConfigImpl extends OpenSDIManagerConfigImpl implements Serializable,
	FileManagerConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6199791588955305910L;

	/**
	 * This text is used as root
	 */
	private String rootText = "/VA-SPs";
	
	/**
	 * Default permission for the folders
	 */
	private FolderPermission defaultPermission;
	
	/**
	 * Permissions by folder name
	 */
	private Map<String, FolderPermission> permissionsByFolder;
	
	/**
	 * Permissions by level
	 */
	private Map<Integer, FolderPermission> permissionsByLevel;
	
	/**
	 * Get folder permission
	 * @param folder
	 * 
	 * @return permissions on the folder
	 */
	public FolderPermission getPermission(String folder) {
		FolderPermission permission = defaultPermission;
		if(permissionsByFolder != null && permissionsByFolder.containsKey(folder)){
			permission = permissionsByFolder.get(folder);
		}else if(permissionsByLevel != null){
			Integer level = folder != null ? folder.split(ControllerUtils.SEPARATOR).length - 1: 0;
			if(permissionsByLevel.containsKey(level)){
				permission = permissionsByLevel.get(level);
			}
		}
		return permission;
	}

	/**
	 * @return the permissionsByLevel
	 */
	public Map<Integer, FolderPermission> getPermissionsByLevel() {
		return permissionsByLevel;
	}

	/**
	 * @param permissionsByLevel the permissionsByLevel to set
	 */
	public void setPermissionsByLevel(
			Map<Integer, FolderPermission> permissionsByLevel) {
		this.permissionsByLevel = permissionsByLevel;
	}

	/**
	 * @return the permissionsByFolder
	 */
	public Map<String, FolderPermission> getPermissionsByFolder() {
		return permissionsByFolder;
	}

	/**
	 * @param permissionsByFolder the permissionsByFolder to set
	 */
	public void setPermissionsByFolder(
			Map<String, FolderPermission> permissionsByFolder) {
		this.permissionsByFolder = permissionsByFolder;
	}

	/**
	 * @return the defaultPermission
	 */
	public FolderPermission getDefaultPermission() {
		return defaultPermission;
	}

	/**
	 * @param defaultPermission the defaultPermission to set
	 */
	public void setDefaultPermission(FolderPermission defaultPermission) {
		this.defaultPermission = defaultPermission;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.config.OpenSDIManagerConfigImpl#getBaseFolder()
	 */
	@Override
	public String getBaseFolder() {
		return super.getBaseFolder();
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.config.OpenSDIManagerConfigImpl#setBaseFolder(java.lang.String)
	 */
	@Override
	public void setBaseFolder(String baseFolder) {
		super.setBaseFolder(baseFolder);
	}

	/**
	 * @return the rootText
	 */
	public String getRootText() {
		return rootText;
	}

	/**
	 * @param rootText the rootText to set
	 */
	public void setRootText(String rootText) {
		this.rootText = rootText;
	}

}
