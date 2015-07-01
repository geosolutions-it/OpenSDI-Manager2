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
package it.geosolutions.nrl.cip.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import it.geosolutions.opensdi.model.Waterflow;

import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class WaterflowDAOTest extends BaseDAOTest {


    public WaterflowDAOTest() {
    }

    @Test
    public void testPersistAndMerge() {

        Long id;

        {
            Waterflow wf = new Waterflow();
            wf.setDecade(123234);
            wf.setDecadeAbsolute(234);
            wf.setDecadeYear(2345);
            wf.setMonth("Mar");
            wf.setRiver("a_pakistan_river");
            wf.setWaterflow(34.5);
            wf.setYear(2015);
            
            waterflowDAO.persist(wf);
            id = wf.getId();
        }
        
        assertNotNull(id);
        LOGGER.info("Saved Waterflow " + id);
        
        {
            //test insert
            Waterflow loaded = waterflowDAO.find(id);
            assertNotNull(loaded);
            assertEquals(new Double(34.5), loaded.getWaterflow());
            assertEquals("a_pakistan_river", loaded.getRiver());
            
            //modify
            loaded.setRiver("another_pakistan_river");
            waterflowDAO.merge(loaded);
        }
        
        {
            //test merge
            Waterflow loaded = waterflowDAO.find(id);
            assertEquals("another_pakistan_river", loaded.getRiver());
        }
    }
    
    @Test
    public void testUniqueness(){
        
        Long id;
        
        {
            Waterflow wf = new Waterflow();
            wf.setDecade(123674);
            wf.setDecadeAbsolute(2224);
            wf.setDecadeYear(234745);
            wf.setMonth("Jun");
            wf.setRiver("a_pakistan_river2");
            wf.setWaterflow(34.5);
            wf.setYear(20152);
            waterflowDAO.persist(wf);
            id = wf.getId();
        }
        
        assertNotNull(id);
        LOGGER.info("Saved Waterflow " + id);
        
        {
            // test insert
            Waterflow loaded = waterflowDAO.find(id);
            assertNotNull(id);
            
            //insert Waterflow with the same data
            try{
                Waterflow wf = new Waterflow();
                wf.setDecade(123674);
                wf.setDecadeAbsolute(2224);
                wf.setDecadeYear(234745);
                wf.setMonth("Jun");
                wf.setRiver("a_pakistan_river2");
                wf.setWaterflow(34.5);
                wf.setYear(20152);
                waterflowDAO.persist(wf);
                fail("Persist() must not allow PK duplicates");
            }
            catch(Exception e){
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }
    }
}







































