/*
 *  Copyright (C) 2013 GeoSolutions S.A.S.
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
package it.geosolutions.nrl.cip.model;

import static org.junit.Assert.assertEquals;
import it.geosolutions.opensdi.model.Season;

import org.junit.Test;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class SeasonTest extends BaseTest {

    public SeasonTest() {
    }

    /**
     * Test of parse method, of class Season.
     */
    @Test
    public void testParseBad() {
        Season result = Season.parse("test string not containing any season");
        assertEquals(Season.NONE, result);
    }

    @Test
    public void testParseBoth() {
        Season result = Season.parse("we have the " + Season.Names.RABI + "and the " +Season.Names.KHARIF+" season");
        assertEquals(Season.RABI_KHARIF, result);
    }

    /**
     * Test of add method, of class Season.
     */
    @Test
    public void testAdd() {
        Season instance = Season.NONE;
        // add RABI, get RABI
        instance = instance.add(Season.RABI);
        assertEquals(Season.RABI, instance);
        // add again RABI, no change
        instance = instance.add(Season.RABI);
        assertEquals(Season.RABI, instance);
        // add NONE, no change
        instance = instance.add(Season.NONE);
        assertEquals(Season.RABI, instance);
        // add KHARIF, get both
        instance = instance.add(Season.KHARIF);
        assertEquals(Season.RABI_KHARIF, instance);
    }

    @Test
    public void testToArray() {
        assertEquals(0, Season.NONE.toArray().length);
        assertEquals(1, Season.KHARIF.toArray().length);
        assertEquals(Season.Names.KHARIF, Season.KHARIF.toArray()[0]);
        assertEquals(1, Season.RABI.toArray().length);
        assertEquals(2, Season.RABI_KHARIF.toArray().length);
    }


}