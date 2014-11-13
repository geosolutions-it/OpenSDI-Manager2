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
package it.geosolutions.opensdi2.configurations.services;

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDEx;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorEx;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundEx;
import it.geosolutions.opensdi2.configurations.model.ConfigDAO;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;

/**
 * @author DamianoG
 *
 * A <b>Configuration Depot<b> in the OpenSDI2 Context is the entry point used by all modules to obtain their own configurations.
 * The Configuration Depot must be unaware of the underlying persistence system used and must use an injected Data Access Object object.
 *   
 * This Interface declares the basic services that the Depot is responsible to provide to the modules and a method to set the DAO dependency.
 *
 */
public interface ConfigDepot {

    /**
     * This method sets the DAO that the Depot will use to read/write configurations
     * 
     * @param osdiDAO
     */
    public void setDao(ConfigDAO osdiDAO);
    
    /**
     * Retrieve a configuration from the depot. Throws an exception if the config isn't found
     * 
     * @param scopeID
     * @param instanceID
     * @return an OpenSDI2 module configuration
     * @throws OSDIConfigurationNotFoundEx if no configuration with the provided scopeID and instanceID is found. 
     */
    public OSDIConfiguration loadExistingConfiguration(String scopeID, String instanceID) throws OSDIConfigurationNotFoundEx;

    /**
     * Add a new a configuration into the depot. Throws an exception if the key is already present
     * 
     * @param conf an OpenSDI2 module configuration to add. The scopeID and instanceID fields must not be null.
     * @throws OSDIConfigurationDuplicatedIDEx if a configuration with the provided scopeID and instanceID is already present 
     */
    public void addNewConfiguration(OSDIConfiguration conf) throws OSDIConfigurationDuplicatedIDEx;

    /** 
     * Update an existing configuration Throws an exception if the config isn't found or if the effective type of the configuration retrieved from the depot is different from the provided one
     * 
     * @param conf a new version of an OpenSDI2 module configuration already present in the system. The scopeID and instanceID fields must not be null. 
     * @throws OSDIConfigurationNotFoundEx  if no configuration with the provided scopeID and instanceID is found.
     * @throws OSDIConfigurationInternalErrorEx if an unexpected error occurs during the update operation 
     */
    public void updateExistingConfiguration(OSDIConfiguration conf)  throws OSDIConfigurationNotFoundEx, OSDIConfigurationInternalErrorEx;

}
