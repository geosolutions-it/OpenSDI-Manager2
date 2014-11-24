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

import it.geosolutions.opensdi2.config.OpenSDIManagerConfigImpl;
import it.geosolutions.opensdi2.configurations.dao.PropertiesDAO;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationDuplicatedIDException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationInternalErrorException;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationNotFoundException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfiguration;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.configurations.model.converters.PropertiesConfigurationConverter;

import java.io.File;

import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class PropertiesDAOTest extends Assert{

    private PropertiesDAO propDAO;
    private File configDir;
    
    public PropertiesDAOTest(){
        try {
            configDir = TestData.file(this, "datadir-testDAOprop");
            OpenSDIManagerConfigImpl configDirHandler = new OpenSDIManagerConfigImpl();
            configDirHandler.setBaseFolder(configDir.getAbsolutePath());
            propDAO = new PropertiesDAO();
            propDAO.setConfigDirManager(configDirHandler);
            propDAO.setConfigBuilder(new PropertiesConfigurationConverter());
            propDAO.init();
        } catch (Exception e) {
            fail();
        }
    }
    
    @Before
    public void initConfigDir() throws OSDIConfigurationDuplicatedIDException, OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException{
        
        try {propDAO.delete("test1", "instance1");} catch (OSDIConfigurationNotFoundException e) {/*Swallow any exception*/}
        try {propDAO.delete("test1", "instance2");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test2", "instance1");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest11");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest12");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest21");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        
        OSDIConfigurationKVP conf1 = new OSDIConfigurationKVP("test1", "instance1");
        conf1.addNew("key1", "test1_value1_instance1");
        conf1.addNew("key2", "test1_value2_instance1");
        conf1.addNew("key3", "test1_value3_instance1");
        conf1.addNew("key4", "test1_value4_instance1");
        propDAO.save(conf1);
        
        OSDIConfigurationKVP conf2 = new OSDIConfigurationKVP("test1", "instance2");
        conf2.addNew("key1", "test1_value1_instance2");
        conf2.addNew("key2", "test1_value2_instance2");
        conf2.addNew("key3", "test1_value3_instance2");
        conf2.addNew("key4", "test1_value4_instance2");
        conf2.addNew("key5", "test1_value5_instance2");
        propDAO.save(conf2);
        
        OSDIConfigurationKVP conf3 = new OSDIConfigurationKVP("test2", "instance1");
        conf3.addNew("key1", "test2_value1_instance1");
        conf3.addNew("key2", "test2_value2_instance1");
        propDAO.save(conf3);
    }
    
    @After
    public void cleanConfigDir() throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException{
        try {propDAO.delete("test1", "instance1");} catch (OSDIConfigurationNotFoundException e) {/*Swallow any exception*/}
        try {propDAO.delete("test1", "instance2");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test2", "instance1");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest11");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest12");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
        try {propDAO.delete("test1", "instanceSaveTest21");} catch (OSDIConfigurationNotFoundException e) {/* Swallow any exception*/}
    }
    
    @Test
    public void loadTest() throws OSDIConfigurationNotFoundException{
        OSDIConfiguration config11 = propDAO.load("test1", "instance1");
        assertEquals(OSDIConfigurationKVP.class, config11.getClass());
        assertEquals(4, ((OSDIConfigurationKVP)config11).getNumberOfProperties());
        String val1 = (String)((OSDIConfigurationKVP)config11).getValue("key1");
        assertEquals("test1_value1_instance1",val1);
        String val3 = (String)((OSDIConfigurationKVP)config11).getValue("key3");
        assertEquals("test1_value3_instance1",val3);
        
        OSDIConfiguration config12 = propDAO.load("test1", "instance2");
        assertEquals(OSDIConfigurationKVP.class, config12.getClass());
        assertEquals(5, ((OSDIConfigurationKVP)config12).getNumberOfProperties());
        val1 = (String)((OSDIConfigurationKVP)config12).getValue("key5");
        assertEquals("test1_value5_instance2",val1);
        
        OSDIConfiguration config21 = propDAO.load("test2", "instance1");
        assertEquals(OSDIConfigurationKVP.class, config21.getClass());
        assertEquals(2, ((OSDIConfigurationKVP)config21).getNumberOfProperties());
        val1 = (String)((OSDIConfigurationKVP)config21).getValue("key1");
        assertEquals("test2_value1_instance1",val1);
        val3 = (String)((OSDIConfigurationKVP)config21).getValue("key2");
        assertEquals("test2_value2_instance1",val3);
        String val29089 = (String)((OSDIConfigurationKVP)config21).getValue("key29089");
        if(val29089 != null){
            fail();
        }
        
        boolean flag = false; 
        try{
            OSDIConfiguration config61 = propDAO.load("test6", "instance1");
        }catch(OSDIConfigurationNotFoundException e){
            flag = true;
            assertEquals("No resource (Module or instance config) with ID 'test6' has been found.", e.getMessage());
        }
        if(!flag){
            fail();
        }
        
        flag = false;
        try{
            OSDIConfiguration config61 = propDAO.load("test2", "instaNce4");
        }catch(OSDIConfigurationNotFoundException e){
            flag = true;
            assertEquals("No resource (Module or instance config) with ID 'instaNce4' has been found.", e.getMessage());
        }
        if(!flag){
            fail();
        }
    }
    
    @Test
    public void saveTest() throws OSDIConfigurationDuplicatedIDException, OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException{
        OSDIConfigurationKVP confKVP11 = new OSDIConfigurationKVP("test1", "instanceSaveTest11");
        confKVP11.addNew("saveKey1", "valuevalue1");
        confKVP11.addNew("saveKey2", "valuevalue2");
        confKVP11.addNew("saveKey3", "valuevalue3");
        confKVP11.addNew("saveKey4", "valuevalue4");
        confKVP11.addNew("saveKey5", "valuevalue5");
        propDAO.save(confKVP11);
        OSDIConfigurationKVP kvpTrgt = (OSDIConfigurationKVP)propDAO.load("test1", "instanceSaveTest11");
        assertEquals(5,kvpTrgt.getNumberOfProperties());
        
        OSDIConfigurationKVP confKVP12 = new OSDIConfigurationKVP("test1", "instanceSaveTest12");
        confKVP12.addNew("saveKey1", "valuevalue1");
        confKVP12.addNew("saveKey2", "valuevalue2");
        propDAO.save(confKVP12);
        kvpTrgt = (OSDIConfigurationKVP)propDAO.load("test1", "instanceSaveTest12");
        assertEquals(2,kvpTrgt.getNumberOfProperties());
        
        OSDIConfigurationKVP confKVP21 = new OSDIConfigurationKVP("test1", "instanceSaveTest21");
        confKVP21.addNew("saveKey1", "valuevalue1");
        confKVP21.addNew("saveKey2", "valuevalue2");
        confKVP21.addNew("saveKey5", "valuevalue5");
        propDAO.save(confKVP21);
        kvpTrgt = (OSDIConfigurationKVP)propDAO.load("test1", "instanceSaveTest21");
        assertEquals(3,kvpTrgt.getNumberOfProperties());
        
        OSDIConfigurationKVP confKVP52 = new OSDIConfigurationKVP("test5", "instanceSaveTest22");
        boolean flag = false;
        try{
            propDAO.save(confKVP52);
        }
        catch(OSDIConfigurationNotFoundException e){
            flag = true;
        }
        if(!flag){
            fail();
        }
        
    }
    
    @Test
    public void updateTest() throws OSDIConfigurationNotFoundException, OSDIConfigurationInternalErrorException{
        OSDIConfigurationKVP config21 = (OSDIConfigurationKVP)propDAO.load("test2", "instance1");
        assertEquals(2,config21.getNumberOfProperties());
        config21.addNew("newKey1", "newValue1");
        config21.addNew("newKey2", "newValue2");
        config21.addNew("newKey3", "newValue3");
        propDAO.merge(config21);
        config21 = null;
        config21 = (OSDIConfigurationKVP)propDAO.load("test2", "instance1");
        assertEquals(5,config21.getNumberOfProperties());
    }
}
