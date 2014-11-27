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

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Base configuration for OpenSDI-Manager2 This bean could centralize all common configuration
 * 
 * @author adiaz
 * 
 */
public class OpenSDIManagerConfigImpl implements Serializable, OpenSDIManagerConfig {

    private static final long serialVersionUID = 4107456302675180556L;

    public static String CONFIGDIR_PROPERTY_ENV_NAME = "OSDI_CONFIG_DIR";

    private File baseFolder;

    private String baseFolderPath;

    /**
     * @param baseFolderPath the baseFolderPath to set
     */
    public void setBaseFolderPath(String baseFolderPath) {
        this.baseFolderPath = baseFolderPath;
    }

    /**
     * @return base folder for the application
     */
    @Override
    public File getConfigDir() {
        return baseFolder;
    }

    @Override
    public void initConfigDir() {
        String configDirPath = System.getProperty(CONFIGDIR_PROPERTY_ENV_NAME);
        if (StringUtils.isBlank(configDirPath)) {
            if (!StringUtils.isBlank(baseFolderPath)) {
                configDirPath = baseFolderPath;
            } else {
                throw new IllegalStateException(
                        "No environment variable '"
                                + CONFIGDIR_PROPERTY_ENV_NAME
                                + "' has been found nor 'baseFolderPath' property has been set , the datadir cannot been loaded so the Application cannot be started.");
            }
        }
        try {
            this.baseFolder = new File(configDirPath);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Exception occurred while loading the config dir from the path '"
                            + configDirPath
                            + "', the datadir cannot been loaded so the Application cannot be started. exception message is: "
                            + e.getMessage());
        }
        if (!this.baseFolder.isDirectory() || !this.baseFolder.canRead()
                || !this.baseFolder.canWrite()) {
            throw new IllegalStateException(
                    "The config dir from the path '"
                            + configDirPath
                            + "' cannot been read or write or it is not a directory... the Application cannot be started.");
        }
    }

}
