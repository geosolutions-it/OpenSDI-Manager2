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

import org.apache.log4j.Logger;

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundEx;
import it.geosolutions.opensdi2.configurations.model.ConfigDAO;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.services.EventsManagerConfigDepot;

/**
 * @author DamianoG
 *
 */
public class MockEventsManagerConfigDepot extends EventsManagerConfigDepot{

    private final static Logger LOGGER = Logger.getLogger(MockEventsManagerConfigDepot.class);
    
    private ConfigDAO dao;
    
    @Override
    public void setDao(ConfigDAO osdiDAO) {
        dao = new MockDAO();
    }

    @Override
    public OSDIConfiguration loadExistingConfiguration(String scopeID, String instanceID)
            throws OSDIConfigurationNotFoundEx {
        OSDIConfiguration config = dao.load(scopeID, instanceID);
        LOGGER.info("config: '" + config.getScopeID() + "-" + config.getInstanceID() + "' Loaded!");
        return config;
    }

    @Override
    protected void addNewConfigurationLogic(OSDIConfiguration config) {
        LOGGER.info("config: '" + config.getScopeID() + "-" + config.getInstanceID() + "' Saved!");
    }

    @Override
    protected void updateExistingConfigurationLogic(OSDIConfiguration config) {
        LOGGER.info("config: '" + config.getScopeID() + "-" + config.getInstanceID() + "' Updated!");
    }
}
