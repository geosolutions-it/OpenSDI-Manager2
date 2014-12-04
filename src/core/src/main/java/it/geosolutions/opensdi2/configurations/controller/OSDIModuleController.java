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
package it.geosolutions.opensdi2.configurations.controller;

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.services.ConfigDepot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * This is the base abstract class for the implementation of a MVC controller / REST interface.
 * It provides support for the module configuration loading in order to allow subclasses to load the configuration in the simplest way avoiding boilerplate code. 
 * All modules involved in OpenSDI2-Manager should implement this class and use its facilities methods. 
 * 
 * @author DamianoG
 *
 */
public abstract class OSDIModuleController{

    private final static Logger LOGGER = Logger.getLogger(OSDIModuleController.class);
    
    public static String SCOPE_ID = "scopeID";
    public static String INSTANCE_ID = "instanceID";
    public static String CONFIGURATION_OBJ_ID = "it.geosolutions.opensdi2.configurations.configuration";
    
    /**
     * The depot, act as facade over the configuration system
     */
    @Autowired
    protected ConfigDepot depot;
    
    
    /**
     * @param Configuration depot the depot to set
     */
    public void setDepot(ConfigDepot depot) {
        this.depot = depot;
    }

    /**
     * The way to get the instance ID is a job for the subclasses since all the controllers are free to handle the instance id as they want.
     * Usually a controller allow the clients to pass the instanceID as a GET parameter, through a REST path placeholder.
     * A controller can also doesn't handle an instanceID at all, in that case simply null should be returned.
     * 
     * @param req 
     * @return an instanceID as a String or simply null
     */
    public abstract String getInstanceID(HttpServletRequest req);
    
    /**
     * 
     * Load the Configuration following these steps:
     * <ul>
     * <li>Load the <b>scopeID</b> taking it from the first placeholder path</li>
     * <li>Load the <b>instanceID</b> calling the abstract method</li>
     * <li>Use that params to load the configuration using the method loadExistingConfiguration</li>
     * </ul>
     * 
     * @param req
     * @return
     * @throws OSDIConfigurationException
     */
    public OSDIConfiguration loadConfiguration(HttpServletRequest req)
            throws OSDIConfigurationException{
        
        String instanceID = getInstanceID(req);
        String scopeID = getScopeID(req);
        
        if(scopeID==null && instanceID==null){
            LOGGER.info("Executing module: '" + req.getPathInfo() + "' ...since both scopeID and instanceID are null NO module configuration will be loaded...");
            return null;
        }
        LOGGER.info("Executing module: '" + req.getPathInfo() + "' configuration with ScopeID: '" + scopeID + "' and instanceID: '" + instanceID + "'");
        OSDIConfiguration tmpConf = new OSDIConfigurationKVP(scopeID, instanceID);
        if(!tmpConf.validateIDs()){
            LOGGER.error("The Configuration Interceptor founds scope and instance IDs that are not valid... Please check your module configurations and installation");
            throw new OSDIConfigurationException("The Configuration Interceptor founds scope and instance IDs that are not valid... Please check your module configurations and installation");
        }
        OSDIConfiguration conf;
        try {
            conf = depot.loadExistingConfiguration(scopeID, instanceID);
        } catch (OSDIConfigurationException e) {
            LOGGER.error("A configuration exception occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
            throw new OSDIConfigurationException("A configuration exception occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
            throw new OSDIConfigurationException("An unexpected error occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
        }
        if(conf == null || !(conf instanceof OSDIConfiguration)){
            LOGGER.error("No exceptions are occurred but the configurations is null or is not an instance of OSDIConfiguration... this should never happens...");
            throw new OSDIConfigurationException("No exceptions are occurred but the configurations is null or is not an instance of OSDIConfiguration... this should never happens...");
        }
        LOGGER.info("Loading the configuration with ScopeID: '" + scopeID + "' and instanceID: '" + instanceID + "' DONE!");
        
        return conf;
    }
    
    private String getScopeID(HttpServletRequest req){
        return req.getParameter(SCOPE_ID);
    }

}
