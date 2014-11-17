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
package it.geosolutions.opensdi2.configurations.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * This class represents the configuration of an OpenSDI module that the configuration depot must handle.
 * This implementation stores the configuration in a key/value data structure in order to avoid the necessity of create and maintain POJO classes to store configurations values.
 * 
 * Basically this class can be used to handle the configuration of any module that can be implemented in the OpenSDI2.
 * As alternative, if the module developer prefers, a POJO approach can be used implementing a module-customized version of OSDIConfiguration (adding getters and setters for all config parameters)  
 * 
 * @author DamianoG
 * 
 */
public class OSDIConfigurationKVP implements OSDIConfiguration{

    private String scopeID;
    private String instanceID;
    
    private Map<String, Object> configParamsMap;
    
    public OSDIConfigurationKVP(String scopeID, String instanceID) throws IllegalArgumentException{
        this.scopeID = scopeID;
        this.instanceID = instanceID;
        if(!validateIDs()){
            throw new IllegalArgumentException("ScopeID or instanceID are null, empty or contains whitespaces");
        }
        this.configParamsMap = new HashMap<String, Object>();
    }
    
    /**
     * It returns the scope identification ID of this configuration.
     *   
     */
    public String getScopeID(){
        return scopeID;
    }
    
    /**
     * It returns the instance identification ID of this configuration within a certain scope.
     * 
     */
    public String getInstanceID(){
        return instanceID;
    }
    
    /**
     * Add a new entry in the configuration
     * 
     * @param key
     * @param value
     */
    public void addNew(String key, Object value){
        configParamsMap.put(key, value);
    }
    
    /**
     * Retrieve a value from the configuration
     * 
     * @param key
     */
    public Object getValue(String key){
        return configParamsMap.get(key);
    }

    public int getNumberOfProperties(){
        return configParamsMap.size();
    }
    
    @Override
    public boolean validateIDs() {
        if(StringUtils.isEmpty(scopeID) || StringUtils.containsWhitespace(scopeID)){
            return false;
        }
        if(StringUtils.isEmpty(instanceID) || StringUtils.containsWhitespace(instanceID)){
            return false;
        }
        return true;
    }
    
}
