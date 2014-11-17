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
import it.geosolutions.opensdi2.configurations.model.converters.OSDIConfigBuilder;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory.FILTER_TYPE;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author DamianoG
 *
 */
public class PropertiesDAO implements ConfigDAO {

    final public static String PROPERTIES_CONFIG_DIR = "propertiesConfigurations";
    
    @Autowired
    private OpenSDIManagerConfig configDirManager;
    
    private OSDIConfigBuilder configBuilder;
    
    private File propertiesConfigDir;
    
    public PropertiesDAO(){
        File baseDir = new File(configDirManager.getBaseFolder());
        if(!basicsDirectoryChecks(baseDir)){
            throw new IllegalStateException("The application DATADIR '" + baseDir + "' doesn't exist or cannot be read or write");
        }
        propertiesConfigDir = new File(baseDir, PROPERTIES_CONFIG_DIR);
        if(!basicsDirectoryChecks(propertiesConfigDir)){
            throw new IllegalStateException("The properties configuration directory inside the DATADIR '" + propertiesConfigDir + "' doesn't exist or cannot be read or write");
        }
    }
    
    @Override
    public void save(OSDIConfiguration config) throws OSDIConfigurationDuplicatedIDException {
        
    }

    @Override
    public boolean merge(OSDIConfiguration config) throws OSDIConfigurationNotFoundException,
            OSDIConfigurationInternalErrorException {

        return false;
    }

    @Override
    public OSDIConfiguration load(String scopeID, String instanceID)
            throws OSDIConfigurationNotFoundException {
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        //Get the correct module configuration directory
        File[] moduleList = propertiesConfigDir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, scopeID));
        assertResourceIsUnique(moduleList, scopeID);
        //Search in the module directory the proper instance configuration file
        File[] instanceList = propertiesConfigDir.listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, instanceID));
        assertResourceIsUnique(instanceList, instanceID);
        //Load the config with apache commons configuration
        Configuration config = null;
        try {
            config = new PropertiesConfiguration(instanceList[0]);
        } catch (ConfigurationException e) {
            throw new OSDIConfigurationNotFoundException(e.getMessage());
        }
        return configBuilder.buildConfig(config, scopeID, instanceID);
    }

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
}
