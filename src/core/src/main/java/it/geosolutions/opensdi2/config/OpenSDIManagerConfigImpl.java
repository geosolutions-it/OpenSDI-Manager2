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

/**
 * Base configuration for OpenSDI-Manager2 This bean could centralize all common configuration
 * 
 * @author adiaz
 * 
 */
public class OpenSDIManagerConfigImpl implements Serializable, OpenSDIManagerConfig {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4107456302675180556L;

    private String baseFolder;

    /**
     * @return base folder for the application
     */
    public String getBaseFolder() {
        return baseFolder;
    }

    /**
     * Setter for the base folder
     * 
     * @param base folder for the application
     */
    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
        if (this.baseFolder != null
                && this.baseFolder.lastIndexOf(ControllerUtils.SEPARATOR) != this.baseFolder
                        .length() - 1) {
            this.baseFolder += ControllerUtils.SEPARATOR;
        }
    }

}
