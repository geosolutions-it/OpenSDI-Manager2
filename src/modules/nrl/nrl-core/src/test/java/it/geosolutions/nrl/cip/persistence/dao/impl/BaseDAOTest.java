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
package it.geosolutions.nrl.cip.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi.model.AgroMet;
import it.geosolutions.opensdi.model.CropData;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.persistence.dao.AgrometDAO;
import it.geosolutions.opensdi.persistence.dao.CropDataDAO;
import it.geosolutions.opensdi.persistence.dao.CropDescriptorDAO;
import it.geosolutions.opensdi.persistence.dao.UnitOfMeasureDAO;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public abstract class BaseDAOTest extends BaseTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected static CropDescriptorDAO cropDescriptorDAO;
    protected static CropDataDAO cropDataDAO;
    protected static AgrometDAO agrometDAO;
    protected static UnitOfMeasureDAO unitDAO;

    public BaseDAOTest() {

        cropDescriptorDAO = ctx.getBean("cropDescriptorDao", CropDescriptorDAO.class);
        cropDataDAO = ctx.getBean("cropDataDao", CropDataDAO.class);
        agrometDAO = ctx.getBean("agrometDao", AgrometDAO.class);
        unitDAO = ctx.getBean("unitOfMeasureDao",UnitOfMeasureDAO.class);
    }
    

    @Before
    public void before() {
        removeAll();
    }

    
    protected void removeAll() {
        removeAllCropData();
        removeAllCropDescriptor();
        removeAllAgromet();
    }

    protected void removeAllCropData() {
        List<CropData> list = cropDataDAO.findAll();
        for (CropData item : list) {
            LOGGER.info("Removing " + item);
            boolean ret = cropDataDAO.remove(item);
            assertTrue("CropData not removed", ret);
        }

        assertEquals("CropData has not been properly deleted", 0, cropDataDAO.count(null));
    }

    protected void removeAllAgromet() {
        List<AgroMet> list = agrometDAO.findAll();
        for (AgroMet item : list) {
            LOGGER.info("Removing " + item);
            boolean ret = agrometDAO.remove(item);
            assertTrue("AgroMet not removed", ret);
        }

        assertEquals("AgroMet has not been properly deleted", 0, agrometDAO.count(null));
    }

    protected void removeAllCropDescriptor() {
        List<CropDescriptor> list = cropDescriptorDAO.findAll();
        for (CropDescriptor item : list) {
            LOGGER.info("Removing " + item);
            boolean ret = cropDescriptorDAO.remove(item);
            assertTrue("CropDescriptor not removed", ret);
        }

        assertEquals("CropDescriptor has not been properly deleted", 0, cropDescriptorDAO.count(null));
    }

    @Test
    public void testCheckDAOs() {

        assertNotNull(cropDescriptorDAO);
        assertNotNull(cropDataDAO);
        assertNotNull(agrometDAO);
    }

//    protected final static String MULTIPOLYGONWKT = "MULTIPOLYGON(((48.6894038 62.33877482, 48.7014874 62.33877482, 48.7014874 62.33968662, 48.6894038 62.33968662, 48.6894038 62.33877482)))";
//    protected final static String POLYGONWKT = "POLYGON((48.6894038 62.33877482, 48.7014874 62.33877482, 48.7014874 62.33968662, 48.6894038 62.33968662, 48.6894038 62.33877482))";
//
//    protected MultiPolygon buildMultiPolygon() {
//        try {
//            WKTReader reader = new WKTReader();
//            MultiPolygon mp = (MultiPolygon) reader.read(MULTIPOLYGONWKT);
//            mp.setSRID(4326);
//            return mp;
//        } catch (ParseException ex) {
//            throw new RuntimeException("Unexpected exception: " + ex.getMessage(), ex);
//        }
//    }
//
//    protected Polygon buildPolygon() {
//        try {
//            WKTReader reader = new WKTReader();
//            Polygon mp = (Polygon) reader.read(POLYGONWKT);
//            mp.setSRID(4326);
//            return mp;
//        } catch (ParseException ex) {
//            throw new RuntimeException("Unexpected exception: " + ex.getMessage(), ex);
//        }
//    }
    //==========================================================================
}
