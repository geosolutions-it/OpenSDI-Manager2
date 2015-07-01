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
import it.geosolutions.opensdi.model.Withdrawal;

import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class WithdrawalDAOTest extends BaseDAOTest {


    public WithdrawalDAOTest() {
    }

    @Test
    public void testPersistAndMerge() {

        Long id;

        {
            Withdrawal wd = new Withdrawal();
            wd.setDecade(44);
            wd.setDecadeAbsolute(22);
            wd.setDecadeYear(333);
            wd.setDistrict("a_pakistan_districy");
            wd.setMonth("Mar");
            wd.setProvince("a_pakistan_province");
            wd.setWithdrawal(45.6);
            wd.setYear(2015);
            
            withdrawalDAO.persist(wd);
            id = wd.getId();
        }
        
        assertNotNull(id);
        LOGGER.info("Saved Withdrawal " + id);
        
        {
            //test insert
            Withdrawal loaded = withdrawalDAO.find(id);
            assertNotNull(loaded);
            assertEquals(new Double(45.6), loaded.getWithdrawal());
            assertEquals("a_pakistan_districy", loaded.getDistrict());
            
            //modify
            loaded.setDistrict("another_pakistan_districy");
            withdrawalDAO.merge(loaded);
        }
        
        {
            //test merge
            Withdrawal loaded = withdrawalDAO.find(id);
            assertEquals("another_pakistan_districy", loaded.getDistrict());
        }
    }
    
    @Test
    public void testUniqueness(){
        
        //@Table(name = "withdrawal", uniqueConstraints = { @UniqueConstraint(columnNames = { "year","month","decade","province","district" }) })
        
        Long id;
        
        {
            Withdrawal wd = new Withdrawal();
            wd.setDecade(22);
            wd.setDecadeAbsolute(32);
            wd.setDecadeYear(87);
            wd.setDistrict("a_pakistan_district");
            wd.setMonth("Jan");
            wd.setProvince("a_pakistan_province");
            wd.setWithdrawal(34.56);
            wd.setYear(2015);
            
            withdrawalDAO.persist(wd);
            id = wd.getId();
        }
        
        assertNotNull(id);
        LOGGER.info("Saved Withdrawal " + id);
        
        {
            // test insert
            Withdrawal loaded = withdrawalDAO.find(id);
            assertNotNull(id);
            
            //insert Withdrawal with the same data
            try{
                Withdrawal wd = new Withdrawal();
                wd.setDecade(22);
                wd.setDecadeAbsolute(32);
                wd.setDecadeYear(87);
                wd.setDistrict("a_pakistan_district");
                wd.setMonth("Jan");
                wd.setProvince("a_pakistan_province");
                wd.setWithdrawal(34.56);
                wd.setYear(2015);
                
                withdrawalDAO.persist(wd);
                fail("Persist() must not allow PK duplicates");
            }
            catch(Exception e){
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }
    }
}







































