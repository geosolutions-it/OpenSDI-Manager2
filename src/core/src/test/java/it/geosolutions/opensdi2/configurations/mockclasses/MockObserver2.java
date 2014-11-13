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

import it.geosolutions.opensdi2.configurations.eventshandling.ConfigDepotObserver;
import it.geosolutions.opensdi2.configurations.eventshandling.Event;

import org.apache.log4j.Logger;

/**
 * @author DamianoG
 *
 */
public class MockObserver2 implements ConfigDepotObserver{

    private final static Logger LOGGER = Logger.getLogger(MockObserver2.class);
    
    public boolean newConfigHandled = false;
    public boolean configUpdatedHandled = false;
    
    @Override
    public Object newConfigAddedEventHandler(Event event) {
        LOGGER.info("instance of MockObserver2 is going to handle a newConfigAdded event...");
        newConfigHandled = true;
        return "MockObserver2-newConfigAddedEvent-handled";
    }

    @Override
    public Object configUpdatedEventHandler(Event event) {
        LOGGER.info("instance of MockObserver2 is going to handle a configUpdatedEvent event...");
        configUpdatedHandled = true;
        return "MockObserver2-configUpdatedEvent-handled";
    }

    
}
