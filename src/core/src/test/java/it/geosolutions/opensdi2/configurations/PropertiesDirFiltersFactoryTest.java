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

import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory.FILTER_TYPE;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class PropertiesDirFiltersFactoryTest extends Assert{

    /**
     * Tests if the builder method returns the right concrete instance
     */
    @Test
    public void builderTest(){
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        FileFilter filter = factory.getFilter(FILTER_TYPE.INSTANCE, "randomParam");
        assertTrue(filter instanceof PropertiesDirFiltersFactory.InstancesConfigFilter);
        
        filter = factory.getFilter(FILTER_TYPE.MODULE, "randomParam");
        assertTrue(filter instanceof PropertiesDirFiltersFactory.ModulesDirFilter);
    }
    
    @Test
    public void moduleLoadTest() throws FileNotFoundException, IOException{
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        FileFilter filter = factory.getFilter(FILTER_TYPE.INSTANCE, "randomParam");
        File datadir = TestData.file(this, "datadir");
        File[] outList = null;
        outList = datadir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, "test1"));
        assertEquals(1,outList.length);
        assertEquals(3,outList[0].listFiles().length);
        outList = datadir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, "test2"));
        assertTrue(outList.length == 1);
        assertEquals(0,outList[0].listFiles().length);
        outList = datadir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, "test3"));
        assertTrue(outList.length == 0);
    }
    
    @Test
    public void instanceLoadTest() throws FileNotFoundException, IOException{
        PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
        File datadir = TestData.file(this, "datadir");
        File[] instanceList = null;
        File[] moduleList = null;
        moduleList = datadir.listFiles(factory.getFilter(FILTER_TYPE.MODULE, "test1"));
        instanceList = moduleList[0].listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, "one"));
        assertEquals(1,instanceList.length);
        instanceList = moduleList[0].listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, "two"));
        assertEquals(1,instanceList.length);
        instanceList = moduleList[0].listFiles(factory.getFilter(FILTER_TYPE.INSTANCE, "three"));
        assertEquals(0,instanceList.length);
    }
}
