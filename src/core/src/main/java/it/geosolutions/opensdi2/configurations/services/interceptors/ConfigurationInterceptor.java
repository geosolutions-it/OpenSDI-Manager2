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
package it.geosolutions.opensdi2.configurations.services.interceptors;

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.services.ConfigDepot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This class is responsible to intercept all incoming HTTP request against an OpenSDI module and load the module 
 * configuration and put it in the request attributes in order to allow the module to load the configuration simply getting it from the request attributes
 * without performing any null or type checks. 
 * 
 * @author DamianoG
 *
 */
public class ConfigurationInterceptor extends HandlerInterceptorAdapter{

    private final static Logger LOGGER = Logger.getLogger(ConfigurationInterceptor.class);
    
    public static String SCOPE_ID = "scopeID";
    public static String INSTANCE_ID = "instanceID";
    public static String CONFIGURATION_OBJ_ID = "it.geosolutions.opensdi2.configurations.configuration";
    private ConfigDepot depot;
    
    
    /**
     * @param Configuration depot the depot to set
     */
    public void setDepot(ConfigDepot depot) {
        this.depot = depot;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            Exception arg3) throws Exception {
        LOGGER.debug("afterCompletion...");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
            ModelAndView arg3) throws Exception {
        LOGGER.debug("postHandle...");
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object model)
            throws Exception{
        LOGGER.debug("preHandle...");
        String scopeID = req.getParameter(SCOPE_ID);
        String instanceID = req.getParameter(INSTANCE_ID);
        if(scopeID==null && instanceID==null){
            LOGGER.info("Executing module: '" + req.getPathInfo() + "' ...since both scopeID and instanceID are null NO module configuration will be loaded...");
            return true;
        }
        LOGGER.info("Executing module: '" + req.getPathInfo() + "' configuration with ScopeID: '" + scopeID + "' and instanceID: '" + instanceID + "'");
        OSDIConfiguration tmpConf = new OSDIConfigurationKVP(scopeID, instanceID);
        if(!tmpConf.validateIDs()){
            LOGGER.error("The Configuration Interceptor founds scope and instance IDs that are not valid... Please check your module configurations and installation");
            return false;
        }
        OSDIConfiguration conf;
        try {
            conf = depot.loadExistingConfiguration(scopeID, instanceID);
        } catch (OSDIConfigurationException e) {
            LOGGER.error("A configuration exception occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
            return false;
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred while loading the configuration, the exception message is: '" + e.getMessage() + "'");
            return false;
        }
        if(conf == null || !(conf instanceof OSDIConfiguration)){
            LOGGER.error("No exceptions are occurred but the configurations is null or is not an instance of OSDIConfiguration... this should never happens...");
            return false;
        }
        req.setAttribute(CONFIGURATION_OBJ_ID, conf);
        LOGGER.info("Loading the configuration with ScopeID: '" + scopeID + "' and instanceID: '" + instanceID + "' DONE!");
        return true;
    }

}
