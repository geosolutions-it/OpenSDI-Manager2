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
package it.geosolutions.opensdi2.configurations.dao;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfig;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.model.converters.OSDIConfigConverter;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory.FILTER_TYPE;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DAta Object implementation of the configuration system persisted on properties files.
 * This implementation uses the library apache commons configuration to deal with properties files management 
 * 
 * @author DamianoG
 *
 */
public class PropertiesDAO implements ConfigDAO {

    public final static String PROPERTIES_CONFIG_DIR = "propertiesConfigurations";
    
    @Autowired
    private OpenSDIManagerConfig configDirManager;
    
    @Autowired
    private OSDIConfigConverter configConverter;
    
    private File propertiesConfigDir;
    
    public PropertiesDAO(){}
    
    @Override
    public void init() {
        File baseDir = new File(configDirManager.getBaseFolder());
        if(!basicsDirectoryChecks(baseDir)){
            throw new IllegalStateException("The application DATADIR '" + baseDir + "' doesn't exist or cannot be read or write");
        }
        propertiesConfigDir = new File(baseDir, PROPERTIES_CONFIG_DIR);
        if(!basicsDirectoryChecks(propertiesConfigDir)){
            throw new IllegalStateException("The properties configuration directory inside the DATADIR '" + propertiesConfigDir + "' doesn't exist or cannot be read or write");
        }
    }
    
    public void setConfigDirManager(OpenSDIManagerConfig manager){
        this.configDirManager = manager;
    }
    
    public void setConfigBuilder(OSDIConfigConverter converter){
        this.configConverter = converter;
    }
    
    // TODO add transactions
    @Override
    public void save(OSDIConfiguration newConfig) throws OSDIConfigurationDuplicatedIDException,
            OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException {
        if (isThisConfigIsAlreadyPresent(newConfig.getScopeID(), newConfig.getInstanceID())) {
            throw new OSDIConfigurationDuplicatedIDException("A configuration with scopeID '"
                    + newConfig.getScopeID() + "' and instanceID '" + newConfig.getInstanceID()
                    + "' is already present.");
        }
        Object configAsParamsSet = configConverter.buildConfig(newConfig);
        PropertiesConfiguration propertiesConfig = (PropertiesConfiguration) configAsParamsSet;

        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        File[] moduleList = propertiesConfigDir.listFiles(factory.getFilter(FILTER_TYPE.MODULE,
                newConfig.getScopeID()));
        File instanceConfig = new File(moduleList[0], factory.INSTANCE_CONFIGNAME_PREFIX
                + newConfig.getInstanceID() + factory.INSTANCE_CONFIGNAME_EXTENSION);
        try {
            propertiesConfig.save(instanceConfig);
        } catch (ConfigurationException e) {
            throw new OSDIConfigurationInternalErrorException(
                    "Error occurred while saving a new configuration, exception msg is: '"
                            + e.getMessage() + "'");
        }
    }

    //TODO add transactions
    @Override
    public boolean merge(OSDIConfiguration updatedConfig) throws OSDIConfigurationNotFoundException,
            OSDIConfigurationInternalErrorException {
        boolean outcome = false;
        File configFile = searchConfigurationFile(updatedConfig.getScopeID(), updatedConfig.getInstanceID());
        PropertiesConfiguration  oldConfig = loadConfigurationInstance(configFile);
        OSDIConfigurationKVP updatedConfigKVP = (OSDIConfigurationKVP)updatedConfig;
        Iterator<String> iter = updatedConfigKVP.getAllKeys().iterator();
        String tmpKey = "";
        while(iter.hasNext()){
            tmpKey = iter.next();
            Object newValue = updatedConfigKVP.getValue(tmpKey);
            Object oldValue = oldConfig.getProperty(tmpKey);
            if(newValue!=null && !newValue.equals(oldValue)){
                oldConfig.setProperty(tmpKey, newValue);
                outcome = true;
            }
        }
        try {
            oldConfig.save();
        } catch (ConfigurationException e) {
            throw new OSDIConfigurationInternalErrorException("Error occurred while saving the updated configuration, exception msg is: '" + e.getMessage() + "'");
        }
        return outcome;
    }

    @Override
    public OSDIConfiguration load(String scopeID, String instanceID)
            throws OSDIConfigurationNotFoundException {
        //Search the configuration file corresponding to the provided scopeID and instanceID inside the datadir
        File configFile = searchConfigurationFile(scopeID, instanceID);
        //Load the config with apache commons configuration
        Configuration config = loadConfigurationInstance(configFile);
        return configConverter.buildConfig(config, scopeID, instanceID);
    }
    
    @Override
    public void delete(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException,
            OSDIConfigurationInternalErrorException {
        // Search the configuration file corresponding to the provided scopeID and instanceID inside the datadir
        File configFile = searchConfigurationFile(scopeID, instanceID);
        if(!configFile.delete()){
            throw new OSDIConfigurationInternalErrorException("Problems while deleting the file configuration for instance with scopeID: '" + scopeID + "' and instanceID: '" + instanceID + "'");
        }
    }

    //
    // PRIVATE INTERNAL UTILITIES METHODs
    //
    
    private boolean basicsDirectoryChecks(File dir) {
        if(dir == null || !dir.exists() || !dir.isDirectory() || !dir.canRead() || !dir.canWrite()){
            return false;
        }
        return true;
    }
    
    private void assertResourceIsUnique(File[] list, String resourceID) throws OSDIConfigurationNotFoundException {
        if(list.length <= 0){
            throw new OSDIConfigurationNotFoundException("No resource (Module or instance config) with ID '" + resourceID + "' has been found.");
        }
        if(list.length > 1){
            throw new OSDIConfigurationNotFoundException("Seems that more than 1 resource with ID '" + resourceID + "' has been found... this should never happen and may means that there's a bug somewhere, open a ticket on the application issue tracker.");
        }
    }
    
    private File searchConfigurationFile(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException{
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        //Get the correct module configuration directory
        File[] moduleList = propertiesConfigDir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, scopeID));
        assertResourceIsUnique(moduleList, scopeID);
        //Search in the module directory the proper instance configuration file
        File[] instanceList = moduleList[0].listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, instanceID));
        assertResourceIsUnique(instanceList, instanceID);
        return instanceList[0];
    }
    
    private PropertiesConfiguration  loadConfigurationInstance(File configFile) throws OSDIConfigurationNotFoundException{
        PropertiesConfiguration  config = null;
        try {
            config = new PropertiesConfiguration(configFile);
        } catch (ConfigurationException e) {
            throw new OSDIConfigurationNotFoundException(e.getMessage());
        }
        return config;
    }
    
    private boolean isThisConfigIsAlreadyPresent(String scopeID, String instanceID) throws OSDIConfigurationNotFoundException{
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        //Get the correct module configuration directory
        File[] moduleList = propertiesConfigDir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, scopeID));
        assertResourceIsUnique(moduleList, scopeID);
        //Search in the module directory the proper instance configuration file
        File[] instanceList = moduleList[0].listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, instanceID));
        if (instanceList.length == 0){
            return false;
        }
        return true;
    }
}
