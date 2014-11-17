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
package it.geosolutions.opensdi2.configurations;

import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.model.converters.OSDIConfigBuilder;
import it.geosolutions.opensdi2.configurations.model.converters.OSDIPropertiesBuilder;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for the implementations of the OSDIConfigBuilder interface
 * 
 * @author DamianoG
 *
 */
public class OSDIConfigurationBuilderTest extends Assert{

    @Test
    public void propertiesBuilderNegativeTest(){
        OSDIConfigBuilder builder = new OSDIPropertiesBuilder();
        boolean failed = false;
        try{
            builder.buildConfig(new String("stringa string !!#*!!"), "scopeID_test", "instanceID_test");
        }
        catch(IllegalArgumentException e){
            failed = true;
        }
        if(!failed){
            fail();
        }
    }
    
    @Test
    public void propertiesBuilderPositiveTest(){
        OSDIConfigBuilder builder = new OSDIPropertiesBuilder();
        Configuration config = new PropertiesConfiguration();
        config.addProperty("key1", "value1");
        config.addProperty("key2", "value2");
        config.addProperty("key3", "value3");
        OSDIConfiguration conf = builder.buildConfig(config, "scopeID_test", "instanceID_test");
        assertTrue(conf instanceof OSDIConfigurationKVP);
        assertEquals(3, ((OSDIConfigurationKVP)conf).getNumberOfProperties());
        assertEquals("value1", ((OSDIConfigurationKVP)conf).getValue("key1"));
        assertEquals("value2", ((OSDIConfigurationKVP)conf).getValue("key2"));
        assertEquals("value3", ((OSDIConfigurationKVP)conf).getValue("key3"));
    }
}
