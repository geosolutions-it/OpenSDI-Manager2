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
package it.geosolutions.opensdi2.configurations.mockclasses;

import it.geosolutions.opensdi2.configurations.dao.ConfigDAO;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;

import org.apache.log4j.Logger;

/**
 * @author DamianoG
 *
 */
public class MockDAO implements ConfigDAO{

    private final static Logger LOGGER = Logger.getLogger(MockDAO.class);
    
    @Override
    public void save(OSDIConfiguration config) throws OSDIConfigurationDuplicatedIDException {
        
        LOGGER.info("config: '" + config.getScopeID() + "-" + config.getInstanceID() + "' Saved!");
    }

    @Override
    public boolean merge(OSDIConfiguration config) throws OSDIConfigurationNotFoundException,
            OSDIConfigurationInternalErrorException {
        
        LOGGER.info("config: '" + config.getScopeID() + "-" + config.getInstanceID() + "' Merged!");
        return true;
    }

    @Override
    public OSDIConfiguration load(String scopeID, String instanceID)
            throws OSDIConfigurationNotFoundException {
        
        OSDIConfigurationKVP osdiConfig = new OSDIConfigurationKVP(scopeID, instanceID);
        osdiConfig.addNew("key1", "value1");
        osdiConfig.addNew("key2", "value2");
        LOGGER.info("config: '" + scopeID + "-" + instanceID + "' Loaded!");
        return osdiConfig;
    }
}
