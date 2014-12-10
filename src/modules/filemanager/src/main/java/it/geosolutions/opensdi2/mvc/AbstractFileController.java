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

import java.io.File;

import it.geosolutions.opensdi2.configurations.controller.OSDIModuleController;
import it.geosolutions.opensdi2.utils.ControllerUtils;

/**
 * Base controller based on files to be extended
 * 
 * @author adiaz
 * @author DamianoG
 * 
 */
public abstract class AbstractFileController extends OSDIModuleController{
	
//	/**
//     * @return the rootDir
//     */
//    public String getRootDir() {
//        return rootDir;
//    }
//
//
//    /**
//     * @param rootDir the rootDir to set
//     */
//    public void setRootDir(String rootDir) {
//        this.rootDir = rootDir;
//    }
//
//
//    private String rootDir;

	
	/**
	 * Obtain file path for a file in a folder inside defaultFolderDir
	 * 
	 * @param fileName
	 * @param subFolder
	 * @return
	 */
	public String getFilePath(String rootDir, String fileName, String subFolder) {
		String filePath = rootDir;
		if (subFolder != null) {
			filePath += subFolder + ControllerUtils.SEPARATOR;
		}
		File f = new File(filePath, fileName);
		filePath = f.getAbsolutePath();
		filePath = ControllerUtils.cleanDuplicateSeparators(filePath);
		return filePath;
	}
	

}
