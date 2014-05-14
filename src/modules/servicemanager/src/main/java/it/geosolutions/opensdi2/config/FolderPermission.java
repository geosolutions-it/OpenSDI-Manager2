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

/**
 * Folder permission for file manager
 * 
 * @author adiaz
 *
 */
public class FolderPermission {
	
	/**
	 * Can create new folders inside the folder
	 */
	boolean canCreateFolder = false;
	
	/**
	 * Can rename files or folders inside the folder
	 */
	boolean canRename = false;
	
	/**
	 * Can delete files or folders inside the folder
	 */
	boolean canDelete = false;
	
	/**
	 * Can upload files inside the folder
	 */
	boolean canUpload = false;
	
	/**
	 * @return the canCreateFolder
	 */
	public boolean canCreateFolder() {
		return canCreateFolder;
	}
	/**
	 * @param canCreateFolder the canCreateFolder to set
	 */
	public void setCanCreateFolder(boolean canCreateFolder) {
		this.canCreateFolder = canCreateFolder;
	}
	/**
	 * @return the canRename
	 */
	public boolean canRename() {
		return canRename;
	}
	/**
	 * @param canRename the canRename to set
	 */
	public void setCanRename(boolean canRename) {
		this.canRename = canRename;
	}
	/**
	 * @return the canDelete
	 */
	public boolean canDelete() {
		return canDelete;
	}
	/**
	 * @param canDelete the canDelete to set
	 */
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	/**
	 * @return the canUpload
	 */
	public boolean canUpload() {
		return canUpload;
	}
	/**
	 * @param canUpload the canUpload to set
	 */
	public void setCanUpload(boolean canUpload) {
		this.canUpload = canUpload;
	}
	
}
