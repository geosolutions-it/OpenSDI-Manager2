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

import it.geosolutions.opensdi2.configurations.eventshandling.ConfigDepotSubscriber;
import it.geosolutions.opensdi2.configurations.eventshandling.Event;
import it.geosolutions.opensdi2.configurations.eventshandling.EventPublisher;
import it.geosolutions.opensdi2.configurations.eventshandling.OSDIEvent;
import it.geosolutions.opensdi2.configurations.eventshandling.SubscriberListManager;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;

import org.apache.log4j.Logger;

/**
 * @author DamianoG
 * 
 * This Class provides the support for event handling to a Depot instance.  
 * Developers should implements the abstract methods {@link #addNewConfiguration(String, String, OSDIConfiguration) and {@link #updateExistingConfiguration(String, String, OSDIConfiguration)} instead of the 
 * related methods defined in the Interface and the event handling comes for free.
 * 
 * The methods {link {@link #loadExistingConfiguration(String, String)} and {link {@link #setDao(it.geosolutions.opensdi2.configurations.dao.ConfigDAO)} must be implemented from scratch. 
 * 
 */
public abstract class PublisherConfigDepot implements ConfigDepot, EventPublisher {

    private final static Logger LOGGER = Logger.getLogger(PublisherConfigDepot.class);
    
    private SubscriberListManager observers;
    
    public PublisherConfigDepot(){
        observers = new SubscriberListManager();
    }

    @Override
    public void addNewConfiguration(OSDIConfiguration config) throws OSDIConfigurationException{
        addNewConfiguration(config, false);
    }

    @Override
    public void addNewConfiguration(OSDIConfiguration config, boolean replace)
            throws OSDIConfigurationException {
        
        if(!config.validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or they contain whitespaces");
        }
        
        try {
            addNewConfigurationLogic(config, replace);
        } catch (OSDIConfigurationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        } catch (OSDIConfigurationInternalErrorException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        } catch (OSDIConfigurationDuplicatedIDException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        }
        catch(Exception e){
            LOGGER.error("An unexpected error occurs while ADDING the new configuration with the following scopeID/instanceID: '" + config.getScopeID() + "/" + config.getInstanceID() +"'");
            throw new OSDIConfigurationException(e);
        }
        //Ok the new configuration should be added correctly, it's time to notify all the observers
        Event e = new OSDIEvent(OSDIEvent.generateEventID(config.getScopeID(), config.getInstanceID()), config.getScopeID(), config.getInstanceID());
        observers.fireNewConfigAddedEvent(e);
    }

    @Override
    public void updateExistingConfiguration(OSDIConfiguration config)
            throws OSDIConfigurationException {
        
        if(!config.validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or they contain whitespaces");
        }
        
        try {
            updateExistingConfigurationLogic(config);
        } catch (OSDIConfigurationNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        } catch (OSDIConfigurationInternalErrorException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OSDIConfigurationException(e);
        }
        catch(Exception e){
            LOGGER.error("An unexpected error occurs while UPDATING the configuration with the following scopeID/instanceID: '" + config.getScopeID() + "/" + config.getInstanceID() +"'");
            throw new OSDIConfigurationException(e);
        }
        //Ok the configuration should be updated correctly, it's time to notify all the observers
        Event e = new OSDIEvent(OSDIEvent.generateEventID(config.getScopeID(), config.getInstanceID()), config.getScopeID(), config.getInstanceID());
        observers.fireConfigUpdatedEventOccurred(e);
    }
    
    @Override
    public boolean subscribe(ConfigDepotSubscriber observer) {
        observers.subscribe(observer);
        return true;
    }
    
    protected abstract void addNewConfigurationLogic(OSDIConfiguration config, boolean replace) throws OSDIConfigurationDuplicatedIDException, OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
    
    protected abstract void updateExistingConfigurationLogic(OSDIConfiguration config) throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException;
}
