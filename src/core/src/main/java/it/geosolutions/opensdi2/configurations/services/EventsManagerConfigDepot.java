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

import it.geosolutions.opensdi2.configurations.eventshandling.ConfigDepotObserver;
import it.geosolutions.opensdi2.configurations.eventshandling.Event;
import it.geosolutions.opensdi2.configurations.eventshandling.EventPublisher;
import it.geosolutions.opensdi2.configurations.eventshandling.OSDIEvent;
import it.geosolutions.opensdi2.configurations.eventshandling.ObserverListManager;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDEx;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorEx;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundEx;
import it.geosolutions.opensdi2.configurations.mockclasses.MockEventsManagerConfigDepot;
import it.geosolutions.opensdi2.configurations.model.ConfigDAO;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;

/**
 * @author DamianoG
 * 
 * This Class provides the support for event handling to a Depot instance.  
 * Developers should implements the abstact methods {@link #addNewConfiguration(String, String, OSDIConfiguration) and {@link #updateExistingConfiguration(String, String, OSDIConfiguration)} instead of the 
 * related methods defined in the Interface and the event handling comes for free.
 * 
 * The methods {link {@link #loadExistingConfiguration(String, String)} and {link {@link #setDao(it.geosolutions.opensdi2.configurations.model.ConfigDAO)} must be implemented from scratch. 
 * 
 */
public abstract class EventsManagerConfigDepot implements ConfigDepot, EventPublisher {

    private final static Logger LOGGER = Logger.getLogger(EventsManagerConfigDepot.class);
    
    private ObserverListManager observers;
    
    public EventsManagerConfigDepot(){
        observers = new ObserverListManager();
    }

    @Override
    public void addNewConfiguration(OSDIConfiguration config)
            throws OSDIConfigurationDuplicatedIDEx {
        
        if(!config.validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or contain whitespaces");
        }
        try{
            addNewConfigurationLogic(config);
        }
        catch(Exception e){
            LOGGER.error("An error occurs while ADDING the new configuration with the following scopeID/instanceID: '" + config.getScopeID() + "/" + config.getInstanceID() +"'");
            LOGGER.error(e.getStackTrace());
            return;
        }
        //Ok the new configuration should be added correctly, it's time to notify all the observers
        Event e = new OSDIEvent(OSDIEvent.generateEventID(config.getScopeID(), config.getInstanceID()), config.getScopeID(), config.getInstanceID());
        observers.fireNewConfigAddedEvent(e);
    }

    @Override
    public void updateExistingConfiguration(OSDIConfiguration config)
            throws OSDIConfigurationNotFoundEx, OSDIConfigurationInternalErrorEx {
        
        if(!config.validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or contain whitespaces");
        }
        try{
            updateExistingConfigurationLogic(config);
        }
        catch(Exception e){
            LOGGER.error("An error occurs while UPDATING the configuration with the following scopeID/instanceID: '" + config.getScopeID() + "/" + config.getInstanceID() +"'");
            LOGGER.error(e.getStackTrace());
            return;
        }
        //Ok the configuration should be updated correctly, it's time to notify all the observers
        Event e = new OSDIEvent(OSDIEvent.generateEventID(config.getScopeID(), config.getInstanceID()), config.getScopeID(), config.getInstanceID());
        observers.fireConfigUpdatedEventOccurred(e);
    }
    
    @Override
    public boolean subscribe(ConfigDepotObserver observer) {
        observers.subscribe(observer);
        return true;
    }
    
    protected abstract void addNewConfigurationLogic(OSDIConfiguration config);
    
    protected abstract void updateExistingConfigurationLogic(OSDIConfiguration config);
}
