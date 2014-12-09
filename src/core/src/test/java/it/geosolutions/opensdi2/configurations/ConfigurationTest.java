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

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.mockclasses.MockObserver1;
import it.geosolutions.opensdi2.configurations.mockclasses.MockObserver2;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.services.PublisherConfigDepot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author DamianoG
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext-configurationsTest.xml")
public class ConfigurationTest extends Assert{
    
    @Autowired
    PublisherConfigDepot depot;
    
    @Autowired
    MockObserver1 mockObs1;
    
    @Autowired
    MockObserver2 mockObs2;
    
    /**
     * Test for basics checks of the spring app-context loading and proper event propagation
     */
    @Test
    public void basicTest(){
        if(depot == null || mockObs1 == null || mockObs2 == null){
            fail();
        }
        depot.subscribe(mockObs1);
        depot.subscribe(mockObs2);
        
        OSDIConfigurationKVP osdiConfig = new OSDIConfigurationKVP("scopeKVP", "instanceKVP");
        osdiConfig.addNew("keyKVP1", "valueKVP");
        osdiConfig.addNew("keyKVP2", "valueKVP2");
        try {
            assertTrue(!mockObs1.newConfigHandled);
            assertTrue(!mockObs2.newConfigHandled);
            depot.addNewConfiguration(osdiConfig);
        } catch (OSDIConfigurationException e) {
            fail();
        }
        assertTrue(mockObs1.newConfigHandled);
        assertTrue(mockObs2.newConfigHandled);
        
        try {
            assertTrue(!mockObs1.configUpdatedHandled);
            assertTrue(!mockObs2.configUpdatedHandled);
            depot.updateExistingConfiguration(osdiConfig);
        } catch (OSDIConfigurationException e) {
            fail();
        }
        assertTrue(mockObs1.configUpdatedHandled);
        assertTrue(mockObs2.configUpdatedHandled);
        
    }
    
    /**
     * Checks if the OSDIConfigurationKVP class works
     */
    @Test
    public void configurationInstantiationTest(){
        
        try{
            new OSDIConfigurationKVP(null,null);
        }
        catch(IllegalArgumentException e1){
            try{
                new OSDIConfigurationKVP("      ","D");
            }
            catch(IllegalArgumentException e2){
                try{
                    new OSDIConfigurationKVP("","SDD");
                }
                catch(IllegalArgumentException e3){
                    try{
                        new OSDIConfigurationKVP("SSS",null);
                    }
                    catch(IllegalArgumentException e4){
                        try{
                            new OSDIConfigurationKVP("ScopeID","Inst anceID");
                        }
                        catch(IllegalArgumentException e5){
                            OSDIConfigurationKVP config = new OSDIConfigurationKVP("scope","instance");
                            config.addNew("param1", "value1");
                            config.addNew("param2", "value2");
                            
                            assertEquals("value1", config.getValue("param1"));
                            assertEquals("value2", config.getValue("param2"));
                            return;
                        }
                    }
                }
            }
        }
        fail();
    }
}
