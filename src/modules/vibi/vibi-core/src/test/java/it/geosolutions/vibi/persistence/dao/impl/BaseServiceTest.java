package it.geosolutions.vibi.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.UnitOfMeasure;
import it.geosolutions.opensdi.service.AgrometDescriptorService;
import it.geosolutions.opensdi.service.CropDescriptorService;
import it.geosolutions.opensdi.service.UnitOfMeasureService;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Base class to test Service tier
 * @author nali
 *
 */
public class BaseServiceTest extends BaseTest{
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	CropDescriptorService cropDescriptorService;
	AgrometDescriptorService agrometDescriptorService;
	UnitOfMeasureService unitOfMeasureService;
	
	public BaseServiceTest() {

		cropDescriptorService = ctx.getBean("cropDescriptorService", CropDescriptorService.class);
        agrometDescriptorService = ctx.getBean("agrometDescriptorService", AgrometDescriptorService.class);
        unitOfMeasureService = ctx.getBean("unitOfMeasureService",UnitOfMeasureService.class);
    }
	
    @Test
    public void testCheckDAOs() {

        assertNotNull(cropDescriptorService);
        assertNotNull(agrometDescriptorService);
        assertNotNull(unitOfMeasureService);
    }
    protected void removeAllCropDescriptors() {
        List<CropDescriptor> list = cropDescriptorService.getAll();
        for (CropDescriptor item : list) {
            LOGGER.info("Removing " + item);
            boolean ret = cropDescriptorService.delete(item.getId());
            assertTrue("Crop Descriptor not removed", ret);
        }
        assertEquals("CropData has not been properly deleted", 0, cropDescriptorService.getCount());
    }
    protected void removeAllUnits() {
        List<UnitOfMeasure> list = unitOfMeasureService.getAll();
        for (UnitOfMeasure item : list) {
            LOGGER.info("Removing " + item);
            boolean ret = unitOfMeasureService.delete(item.getId());
            assertTrue("Unit not removed", ret);
        }
        assertEquals("CropData has not been properly deleted", 0, unitOfMeasureService.getCount());
    }
    
}
