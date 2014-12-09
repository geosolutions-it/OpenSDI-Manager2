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
package it.geosolutions.opensdi2.configurations.dao;

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;

/**
 * 
 * Basics operation to perform on the persisted configuration.
 * Each underlying persistence system used (f.e. XMLs on Filesystem or DBMS) should provide a custom implementation of this interface and inject it in the ConfigDepot used
 * 
 * @author DamianoG
 * 
 */
public interface ConfigDAO {

    /**
     * Perform the initialization of the connection resources with the underlying persistence system  
     */
    public void init();
    
    /**
     * Persist a new configuration
     * 
     * @param newConfig
     * @throws OSDIConfigurationDuplicatedIDException if a configuration with the same ID already exist
     * @throws OSDIConfigurationNotFoundException if the module with the scopeID stored in the newConfig object provided doesn't still exist  
     */
    public void save(OSDIConfiguration newConfig) throws OSDIConfigurationDuplicatedIDException, OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
    
    /**
     * Merge an in-memory modified configuration with the related persisted configuration
     * 
     * @param updatedConfig
     * @return true if the merge changed at least one stored value, false if nothing happened
     * @throws OSDIConfigurationNotFoundException  if no configuration with the provided scopeID and instanceID is found.
     * @throws OSDIConfigurationInternalErrorException if an unexpected error occurs during the update operation
     */
    public boolean merge(OSDIConfiguration updatedConfig) throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
    
    /**
     * Load the requested configuration
     * 
     * @param scopeID
     * @param instanceID
     * @throws OSDIConfigurationNotFoundException  if no configuration with the provided scopeID and instanceID is found.
     */
    public OSDIConfiguration load(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException;
    
    /**
     * 
     * @param scopeID
     * @param instanceID
     * @throws OSDIConfigurationNotFoundException  if no configuration with the provided scopeID and instanceID is found.
     * @throws OSDIConfigurationInternalErrorException if a I/O error occurs while deleting the object
     */
    public void delete(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
}
