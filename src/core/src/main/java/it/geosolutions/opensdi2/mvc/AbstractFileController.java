/*
 *  OpenSDI Manager 2
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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfig;
import it.geosolutions.opensdi2.utils.ControllerUtils;

import javax.annotation.Resource;

/**
 * Base controller based on files to be extended
 * 
 * @author adiaz
 * 
 */
public abstract class AbstractFileController {
	
	private String runtimeDir;

	
	/**
	 * Obtain file path for a file in a folder inside defaultFolderDir
	 * 
	 * @param fileName
	 * @param subFolder
	 * @return
	 */
	public String getFilePath(String fileName, String subFolder) {
		String filePath = getRuntimeDir();
		if (subFolder != null) {
			filePath += subFolder + ControllerUtils.SEPARATOR;
		}
		filePath += fileName;
		filePath = ControllerUtils.cleanDuplicateSeparators(filePath);
		return filePath;
	}

	/**
	 * @return the config
	 */
	public String getRuntimeDir() {
		return this.runtimeDir;
	}

	/**
	 * @param config the config to set
	 */
	public void setRuntimeDir(String runtimeDir){
		this.runtimeDir = runtimeDir;
	}
	

}
