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
     * Persist a new configuration
     * 
     * @param config
     * @throws OSDIConfigurationDuplicatedIDException if a configuration with the same ID already exist  
     */
    public void save(OSDIConfiguration config) throws OSDIConfigurationDuplicatedIDException;
    
    /**
     * Merge an in-memory modified configuration with the related persisted configuration
     * 
     * @param config
     * @return true if the merge changed at least one stored value, false if nothing happened
     * @throws OSDIConfigurationNotFoundException  if no configuration with the provided scopeID and instanceID is found.
     * @throws OSDIConfigurationInternalErrorException if an unexpected error occurs during the update operation
     */
    public boolean merge(OSDIConfiguration config) throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
    
    /**
     * Load the requested configuration
     * 
     * @param scopeID
     * @param instanceID
     * @throws OSDIConfigurationNotFoundException  if no configuration with the provided scopeID and instanceID is found.
     */
    public OSDIConfiguration load(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException;
}
