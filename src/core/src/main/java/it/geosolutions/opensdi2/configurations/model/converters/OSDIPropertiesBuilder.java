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
package it.geosolutions.opensdi2.configurations.model.converters;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;

import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;

/**
 * This class provide a "properties file-oriented" implementation for the OSDIConfigBuilder interface
 * 
 * @author DamianoG
 * 
 */
public class OSDIPropertiesBuilder implements OSDIConfigBuilder {

    /**
     * This implementation expects an input object instance of org.apache.commons.configuration.Configuration otherwise an unchecked exception is
     * thrown. The concrete return type is OSDIConfigurationKVP
     */
    @Override
    public OSDIConfiguration buildConfig(Object configToBeConverted, String scopeID,
            String instanceID) {
        if (!(configToBeConverted instanceof Configuration)) {
            throw new IllegalArgumentException(
                    "You are using the class OSDIPropertiesConverter as implementation of the OSDIConfigConverter interface, you need to pass to this method an instance of org.apache.commons.configuration.Configuration");
        }
        Configuration inConfig = (Configuration) configToBeConverted;
        OSDIConfigurationKVP outConfig = new OSDIConfigurationKVP(scopeID, instanceID);

        Iterator<String> iter = inConfig.getKeys();
        String tmpKey = "";
        while (iter.hasNext()) {
            tmpKey = iter.next();
            outConfig.addNew(tmpKey, inConfig.getProperty(tmpKey));
        }
        return outConfig;
    }

}
