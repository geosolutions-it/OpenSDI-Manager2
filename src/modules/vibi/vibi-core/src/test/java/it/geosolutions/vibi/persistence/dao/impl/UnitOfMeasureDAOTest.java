package it.geosolutions.vibi.persistence.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import it.geosolutions.opensdi.model.UnitOfMeasure;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the unit of measure DAO
 * 
 * @author Lorenzo Natali, GeoSolutions
 * 
 */
public class UnitOfMeasureDAOTest extends BaseDAOTest {
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

		String ID = "TEST_UNIT";
		String TEST_MODIFY = "TEST_MODIFIED";

        {
        	 
             String cls = "production";
             double factor = 1.0f;
            UnitOfMeasure uom = quickCreateUom(ID,cls,factor);
            unitDAO.persist(uom);
            String id = uom.getId();
            assertNotNull(id);
            UnitOfMeasure persisted = unitDAO.find(ID);
            assertEquals(ID, persisted.getId());
            assertEquals(factor, persisted.getCoefficient(),0);
            LOGGER.info("Saved unit: " + id);
        }

        {
            // test insert
        	
        	UnitOfMeasure loaded = unitDAO.find(ID);
            assertNotNull(loaded);
            

            // and modify
            loaded.setDescription(TEST_MODIFY);
            unitDAO.merge(loaded);
            
        }

        {
            // test merge
        	UnitOfMeasure loaded = unitDAO.find(ID);
            assertNotNull(loaded);
            assertEquals(TEST_MODIFY, loaded.getDescription());
        }

    }

	/**
	 * Create fake unit of measure quickly
	 * 
	 * @param id
	 *            used for id and label
	 * @param cls
	 *            the class
	 * @return
	 */
	protected UnitOfMeasure quickCreateUom(String id, String cls, double coeff) {
		UnitOfMeasure u = new UnitOfMeasure();
		u.setCls(cls);
		u.setDescription("Test unit of measure for unit tests");
		u.setId(id);
		u.setName(id);
		u.setShortname(id);
		u.setCoefficient(coeff);
		return u;

	}

}
