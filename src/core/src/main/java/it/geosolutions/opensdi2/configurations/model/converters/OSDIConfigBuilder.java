/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.configurations.model.converters;

import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;

/**
 * This interface define the basic operations that the OpenSDI2manager must perform to convert a loaded configuration from the underlying configuration system
 * 
 * @author DamianoG
 *
 */
public interface OSDIConfigBuilder {

    /**
     * Convert the provided input configuration in an OSDIConfiguration 
     * 
     * @param configToBeConverted
     * @return
     */
    public OSDIConfiguration buildConfig(Object configToBeConverted, String scopeID, String instanceID);
}
