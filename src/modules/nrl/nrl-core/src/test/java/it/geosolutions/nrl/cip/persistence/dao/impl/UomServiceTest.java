package it.geosolutions.nrl.cip.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.Season;
import it.geosolutions.opensdi.model.UnitOfMeasure;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Test for Service tier of unit of measure persistence. 
 * tests service tier specific parts only
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class UomServiceTest extends BaseServiceTest {
	private static final String CROP = "TEST_CROP";
	private static final String UOM_PROD = "TEST_UOM_PROD";
	private static final String UOM_AREA = "TEST_UOM_AREA";
	private static final String UOM_YIELD = "TEST_UOM_YIELD";

	@BeforeClass
	public static void setUpClass() {
		
	}

	@AfterClass
	public static void tearDownClass() {
		
	}

	@Before
	public void setUp() {
		createCropDescriptor(CROP, createUom(UOM_PROD, "production", 0.5),
				createUom(UOM_AREA, "area", 0.5),
				createUom(UOM_YIELD, "yield", 0.5));
	}

	@After
	public void tearDown() {
		removeAllCropDescriptors();
		removeAllUnits();
	}

	@Test
	public void testUomService() {
		List<CropDescriptor> list = cropDescriptorService.getAll();
		for(CropDescriptor cd: list){
			testUnitsForCrop(cd);
		}
	}

	private void testUnitsForCrop(CropDescriptor cd) {
		assertEquals(UOM_AREA,unitOfMeasureService.getDefaultAreaUnitOfMeasure(cd.getId()).getId());
		assertEquals(UOM_PROD,unitOfMeasureService.getDefaultProductionUnitOfMeasure(cd.getId()).getId());
		assertEquals(UOM_YIELD,unitOfMeasureService.getDefaultYieldUnitOfMeasure(cd.getId()).getId());
		
		
	}

	/**
	 * Create a crop Descriptor
	 * 
	 * @param id
	 *            the id
	 * @return the crop Descriptor
	 */
	protected CropDescriptor createCropDescriptor(String id,
			UnitOfMeasure prod, UnitOfMeasure area, UnitOfMeasure yield) {
		// create base cropdescriptor
		CropDescriptor descriptor = new CropDescriptor();
		descriptor.setId(id);
		descriptor.setLabel(id);
		descriptor.setSeasons(Season.RABI_KHARIF);
		descriptor.setProd_default_unit(prod.getId());
		descriptor.setArea_default_unit(area.getId());
		descriptor.setYield_default_unit(yield.getId());

		cropDescriptorService.persist(descriptor);
		return descriptor;
	}

	protected UnitOfMeasure createUom(String id, String cls, double factor) {
		UnitOfMeasure u = new UnitOfMeasure();
		u.setCls(cls);
		u.setDescription("Test unit of measure for unit tests");
		u.setId(id);
		u.setName(id);
		u.setShortname(id);
		u.setCoefficient(factor);
		unitOfMeasureService.persist(u);
		return unitOfMeasureService.get(id);
	}
}
