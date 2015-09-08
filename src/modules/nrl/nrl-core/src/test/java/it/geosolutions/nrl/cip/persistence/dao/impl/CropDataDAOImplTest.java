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
import static org.junit.Assert.fail;
import it.geosolutions.opensdi.model.CropData;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.Season;

import org.junit.Test;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class CropDataDAOImplTest extends BaseDAOTest {

    private static final String CROPNAME = "crop0";

    public CropDataDAOImplTest() {
    }

    @Test
    public void testPersistAndMerge() {

        Long id;

        {
            CropData cd = new CropData();
            cd.setCropDescriptor(createCropDescriptor());
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            
            cd.setArea(100d);
            cd.setProduction(200d);
            cd.setYears("2000-2013");
            cd.setYield(300d);

            cropDataDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved CropData " + id);

        {
            // test insert
            CropData loaded = cropDataDAO.find(id);
            assertNotNull(loaded);
            assertEquals(CROPNAME, loaded.getCropDescriptor().getId());
            assertEquals(Double.valueOf(100d), loaded.getArea());

            // and modify
            loaded.setArea(111d);
            cropDataDAO.merge(loaded);
        }

        {
            // test merge
            CropData loaded = cropDataDAO.find(id);
            assertNotNull(loaded);
            assertEquals(Double.valueOf(111d), loaded.getArea());
        }

    }
    
    @Test
    public void testSearchByPK() {

        Long id;
        String cropDId;
        {
            CropData cd = new CropData();
            CropDescriptor cropd = createCropDescriptor();
            cropDId = cropd.getId();
            cd.setCropDescriptor(cropd);
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            
            cd.setArea(100d);
            cd.setProduction(200d);
            cd.setYears("2000-2013");
            cd.setYield(300d);
            cd.setSrc("source");
            cropDataDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved CropData " + id);

        {
            // test searchByPk
            cropDataDAO.setSrc("source");
            CropData loaded = cropDataDAO.searchByPK(cropDId,"distr","prov",2013,null);
            assertNotNull(loaded);
            assertEquals(CROPNAME, loaded.getCropDescriptor().getId());
            assertEquals(Double.valueOf(100d), loaded.getArea());

        }
    }
    
    @Test
    public void testRemoveByPK() {

        Long id;
        String cropDId;
        {
            CropData cd = new CropData();
            CropDescriptor cropd = createCropDescriptor();
            cropDId = cropd.getId();
            cd.setCropDescriptor(cropd);
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);
            
            cd.setArea(100d);
            cd.setProduction(200d);
            cd.setYears("2000-2013");
            cd.setYield(300d);
            cd.setSrc("source");
            cropDataDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved CropData " + id);

        {
            // test searchByPk
            cropDataDAO.setSrc("source");
            boolean removed = cropDataDAO.removeByPK(cropDId,"distr","prov",2013,null);
            assertEquals(true, removed);
            CropData loaded = cropDataDAO.searchByPK(cropDId,"distr","prov",2013,null);
            if(loaded!=null){
                fail();
            }
        }
    }

    @Test
    public void testUpdatability() {

        Long id;

        {
            CropData cd = new CropData();
            cd.setCropDescriptor(createCropDescriptor());
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);

            cd.setArea(100d);
            cd.setProduction(200d);
            cd.setYears("2000-2013");
            cd.setYield(300d);

            cropDataDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved CropData " + id);

        {
            // test insert
            CropData loaded = cropDataDAO.find(id);
            assertNotNull(loaded);

            // and modify
            CropDescriptor anotherCrop = createCropDescriptor("anotherDescriptor");
            loaded.setCropDescriptor(anotherCrop);
            try {
                cropDataDAO.merge(loaded);
//                fail("Update should not be allowed for crop field");
                LOGGER.error("Update should not be allowed for crop field");
            } catch (Exception e) {
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }

        {
            // test insert
            CropData loaded = cropDataDAO.find(id);
            assertNotNull(loaded);
            assertEquals(CROPNAME, loaded.getCropDescriptor().getId());
        }
    }

    @Test
    public void testUniqueness() {

        Long id;
        CropDescriptor cropDescriptor = createCropDescriptor();

        {
            CropData cd = new CropData();
            cd.setCropDescriptor(cropDescriptor);
            cd.setDistrict("distr");
            cd.setProvince("prov");
            cd.setYear(2013);

            cd.setArea(100d);
            cd.setProduction(200d);
            cd.setYears("2000-2013");
            cd.setYield(300d);
            cd.setSrc("testSrc");
            
            cropDataDAO.persist(cd);
            id = cd.getId();
        }

        assertNotNull(id);
        LOGGER.info("Saved CropData " + id);

        {
            // test insert
            CropData loaded = cropDataDAO.find(id);
            assertNotNull(loaded);

            // insert Crop with same data
            try {
                CropData cd = new CropData();
                cd.setCropDescriptor(cropDescriptor);
                cd.setDistrict("distr");
                cd.setProvince("prov");
                cd.setYear(2013);

                cd.setArea(0d);
                cd.setProduction(0d);
                cd.setYears("-");
                cd.setYield(0d);
                cd.setSrc(null);
                cd.setSrc("testSrc");
                
                cropDataDAO.persist(cd);
                
                fail("Persist() should not allow PK duplicates");
            } catch (Exception e) {
                LOGGER.info("Expected exception thrown : " + e.getMessage());
            }
        }
    }

//    @Test
//    public void testPersistProvOnly() {
//
//        Long id;
//
//        {
//            CropData cd = new CropData();
//            cd.setCropDescriptor(createCropDescriptor());
//            cd.setDistrict(null);
//            cd.setProvince("prov");
//            cd.setYear(2013);
//
//            cd.setArea(100d);
//            cd.setProduction(200d);
//            cd.setYears("2000-2013");
//            cd.setYield(300d);
//
//            cropDataDAO.persist(cd);
//            id = cd.getId();
//        }
//        assertNotNull(id);
//    }

//    @Test
//    public void testPersistDistrOnly() {
//
//        Long id;
//
//        {
//            CropData cd = new CropData();
//            cd.setCropDescriptor(createCropDescriptor());
//            cd.setDistrict("distr");
//            cd.setProvince(null);
//            cd.setYear(2013);
//
//            cd.setArea(100d);
//            cd.setProduction(200d);
//            cd.setYears("2000-2013");
//            cd.setYield(300d);
//
//            cropDataDAO.persist(cd);
//            id = cd.getId();
//        }
//        assertNotNull(id);
//    }

    @Test
    public void testPersistNoProvDistr() {

        CropData cd = new CropData();
        cd.setCropDescriptor(createCropDescriptor());
        cd.setDistrict(null);
        cd.setProvince(null);
        cd.setYear(2013);

        cd.setArea(100d);
        cd.setProduction(200d);
        cd.setYears("2000-2013");
        cd.setYield(300d);

        try {
            cropDataDAO.persist(cd);
            fail("Null fields not recognized");
        } catch(Exception e) {
            LOGGER.info("Bad use case properly trapped: " + e.getMessage());
        }
    }
    /**
     * Create a crop Descriptor 
     * @param id the id 
     * @return the crop Descriptor
     */
    protected CropDescriptor createCropDescriptor(String id) {
        // create base cropdescriptor
        CropDescriptor descriptor = new CropDescriptor();
        descriptor.setId(id);
        descriptor.setLabel(id);
        descriptor.setSeasons(Season.RABI_KHARIF);
        descriptor.setArea_default_unit("000_ha");
        descriptor.setProd_default_unit("000_tons)");
        descriptor.setYield_default_unit("kg_ha");

        cropDescriptorDAO.persist(descriptor);
        return descriptor;
    }

    protected CropDescriptor createCropDescriptor() {
        return createCropDescriptor(CROPNAME);
    }
}