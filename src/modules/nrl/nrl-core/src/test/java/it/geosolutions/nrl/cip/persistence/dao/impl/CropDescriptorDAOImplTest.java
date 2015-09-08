/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.Season;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class CropDescriptorDAOImplTest extends BaseDAOTest {

    public CropDescriptorDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testPersistAndMerge() {

        String ID = "TEST00";
        String id;

        {
            CropDescriptor cd = new CropDescriptor();
            cd.setId(ID);
            cd.setSeasons(Season.RABI_KHARIF);
            cd.setLabel("test descriptor");
            cd.setArea_default_unit("000_ha");
            cd.setProd_default_unit("000_tons)");
            cd.setYield_default_unit("kg_ha");

            cropDescriptorDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        assertEquals(ID, id);

        LOGGER.info("Saved cropdescriptor " + id);

        {
            // test insert
            CropDescriptor loaded = cropDescriptorDAO.find(id);
            assertNotNull(loaded);
            assertEquals("test descriptor", loaded.getLabel());

            // and modify
            loaded.setLabel("test2");
            cropDescriptorDAO.merge(loaded);
        }

        {
            // test merge
            CropDescriptor loaded = cropDescriptorDAO.find(id);
            assertNotNull(loaded);
            assertEquals("test2", loaded.getLabel());
        }

    }
}