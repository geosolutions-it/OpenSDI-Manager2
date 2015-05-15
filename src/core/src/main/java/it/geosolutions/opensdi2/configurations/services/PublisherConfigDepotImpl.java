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

import org.apache.log4j.Logger;

import it.geosolutions.opensdi2.configurations.dao.ConfigDAO;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;

/**
 * @author DamianoG
 *
 */
public class PublisherConfigDepotImpl extends PublisherConfigDepot{

    private final static Logger LOGGER = Logger.getLogger(PublisherConfigDepotImpl.class);
    
    private ConfigDAO dao;
    
    @Override
    public void setDao(ConfigDAO osdiDAO) {
        this.dao = osdiDAO;
    }

    @Override
    public OSDIConfiguration loadExistingConfiguration(String scopeID, String instanceID)
            throws OSDIConfigurationException {
        //create a temporary config only to check the ID validity
        OSDIConfiguration tmpConf = new OSDIConfigurationKVP(scopeID, instanceID);
        if(!tmpConf.validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or they contain whitespaces");
        }
        OSDIConfiguration config;
        try {
            config = dao.load(scopeID, instanceID);
        } catch (OSDIConfigurationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        } catch(Exception e){
            LOGGER.error("An unexpected error occurs while UPDATING the configuration with the following scopeID/instanceID: '" + tmpConf.getScopeID() + "/" + tmpConf.getInstanceID() +"'");
            throw new OSDIConfigurationException(e);
        }
        return config;
    }

    @Override
    protected void addNewConfigurationLogic(OSDIConfiguration config, boolean replace) throws OSDIConfigurationDuplicatedIDException, OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException {
        
        if(replace){
            dao.delete(config.getScopeID(), config.getInstanceID());
        }
        dao.save(config);
    }

    @Override
    protected void updateExistingConfigurationLogic(OSDIConfiguration config) throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException {
        dao.merge(config);   
    }

}
