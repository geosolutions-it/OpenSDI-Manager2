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
package it.geosolutions.opensdi.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import it.geosolutions.opensdi.model.AgroMet;

import org.junit.Test;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class AgrometDAOTest extends BaseDAOTest {


    private static final String FACTOR = "factor0";

    public AgrometDAOTest() {
    }

    @Test
    public void testPersistAndMerge() {

        Long id;

        {
            AgroMet cd = new AgroMet();
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            cd.setMonth("Jan");
            cd.setDec(0);
            cd.setFactor(FACTOR);
            cd.setValue(42.0);

            agrometDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved AgroMet " + id);

        {
            // test insert
            AgroMet loaded = agrometDAO.find(id);
            assertNotNull(loaded);
            assertEquals("Jan", loaded.getMonth());
            assertEquals(42.0, loaded.getValue(), 0);

            // and modify
            loaded.setValue(111d);
            agrometDAO.merge(loaded);
        }

        {
            // test merge
            AgroMet loaded = agrometDAO.find(id);
            assertNotNull(loaded);
            assertEquals(111d, loaded.getValue(), 0);
        }

    }

    @Test
    public void testUpdatability() {

         Long id;

        {
            AgroMet cd = new AgroMet();
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            cd.setMonth("Jan");
            cd.setDec(0);
            cd.setFactor(FACTOR);
            cd.setValue(42.0);

            agrometDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved AgroMet " + id);

        {
            // test insert
            AgroMet loaded = agrometDAO.find(id);
            assertNotNull(loaded);

            // and modify
            loaded.setMonth("Feb");
            try {
                agrometDAO.merge(loaded);
//                LOGGER.error("Update should not be allowed for month field");
//                fail("Update should not be allowed for month field");
            } catch (Exception e) {
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }
        {
            AgroMet loaded = agrometDAO.find(id);
            assertNotNull(loaded);
            assertEquals("Jan", loaded.getMonth());
        }

    }

    @Test
    public void testUniqueness() {

        // @UniqueConstraint(columnNames = {"factor" , "district" , "province" , "year", "month", "dek"})})

         Long id;

        {
            AgroMet cd = new AgroMet();
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            cd.setMonth("Jan");
            cd.setDec(0);
            cd.setFactor(FACTOR);
            cd.setValue(42.0);

            agrometDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved AgroMet " + id);;

        {
            // test insert
            AgroMet loaded = agrometDAO.find(id);
            assertNotNull(loaded);

            // insert Crop with same data
            try {
                AgroMet cd = new AgroMet();
                cd.setDistrict("distr");
                cd.setProvince("prov");
                cd.setYear(2013);
                cd.setMonth("Jan");
                cd.setDec(0);
                cd.setFactor(FACTOR);
                cd.setValue(999.0);

                agrometDAO.persist(cd);

                fail("Persist() should not allow PK duplicates");
            } catch (Exception e) {
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }
    }

}