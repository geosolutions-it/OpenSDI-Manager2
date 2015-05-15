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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
     * <li><strong>If the instanceID is null (or blank)</strong> it will initialized as the scopeID... this case means that the module has only one configuration, called as the module name</li>
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
        
        if(StringUtils.isBlank(scopeID)){
            LOGGER.info("Executing module: '" + req.getPathInfo() + "' ...ScopeID is null or blank... This should never happen... Have you overrode the getScopeID method?...");
            throw new OSDIConfigurationException("ScopeID is null or blank... This should never happen... Have you overrode the getScopeID method?");
        }
        if(StringUtils.isBlank(instanceID)){
            instanceID = scopeID;
            LOGGER.warn("The instanceID is null or blank and it will be set as the scopeID... A configuration file called as the scopeID is expected...");
        }
        LOGGER.info("Executing module: '" + req.getPathInfo() + "' configuration with ScopeID: '" + scopeID + "' and instanceID: '" + instanceID + "'");
        OSDIConfiguration tmpConf = new OSDIConfigurationKVP(scopeID, instanceID);
        if(!tmpConf.validateIDs()){
            LOGGER.error("A scope and instance IDs are not valid... Please check your module configurations and installation");
            throw new OSDIConfigurationException("A scope and instance IDs are not valid... Please check your module configurations and installation");
        }
        OSDIConfiguration conf = null;
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
    
    /**
     * It returns the scopeID extracting the first part of the path after the servlet path.
     * If the module is implemented as a spring controller (as suggested) the method request.getPathInfo() 
     * used in this method internally returns the path after the the URL of the SpringMVC Dispatcher Servlet.
     * So the first part of the result will be for sure the name of the module as defined by convention.
     * 
     * the URL of the SpringMVC Dispatcher Servlet
     * 
     * @param req
     * @return
     */
    protected String getScopeID(HttpServletRequest req){
        String path = req.getPathInfo();
        if(path == null){
            throw new IllegalArgumentException("The path found in the request is null... this should never happen...");
        }
        LOGGER.debug("Extracting first part of the following path '" + path + "' in order to get the module name...");
        String [] parts = path.split("/");
        if(parts == null || parts.length == 0 || parts[0] == null){
            throw new IllegalArgumentException("no scopeID is found... this should never happen...");
        }
        if(StringUtils.isNotEmpty(parts[0])){
            return parts[0];
        }
        for(int i=0; i<parts.length; i++){
            if(StringUtils.isNotEmpty(parts[i])){
                return parts[i];
            }
        }
        String scopeID = getPathFragment(req, 0);
        if(scopeID == null) {
        	throw new IllegalArgumentException("no scopeID is found after all the possible attemps... this should never happen...");
        }
        return scopeID;
    }
    
    protected String getPathFragment(HttpServletRequest req, int index) {
    	String path = req.getPathInfo();
        if(path == null){
            throw new IllegalArgumentException("The path found in the request is null... this should never happen...");
        }
        LOGGER.debug("Extracting part of the following path '" + path + "' in order to get the module name...");
        path = StringUtils.removeStart(path, "/");
        path = StringUtils.removeEnd(path, "/");
        
        String [] parts = path.split("/");
        if(parts == null || parts.length <= index || parts[index] == null){
            throw new IllegalArgumentException("no fragment is found... this should never happen...");
        }
        if(StringUtils.isNotEmpty(parts[index])){
            return parts[index];
        }
        for(int i=index; i<parts.length; i++){
            if(StringUtils.isNotEmpty(parts[i])){
                return parts[i];
            }
        }
        return null;
    }
    
}
