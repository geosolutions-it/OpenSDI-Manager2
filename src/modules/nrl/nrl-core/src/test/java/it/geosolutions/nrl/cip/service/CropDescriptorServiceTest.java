/**
 * 
 */
package it.geosolutions.nrl.cip.service;

import it.geosolutions.opensdi.persistence.dao.CropDescriptorDAO;
import it.geosolutions.opensdi.service.CropDescriptorService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Admin
 *
 */
public class CropDescriptorServiceTest extends BaseTest{
	@Autowired
	public CropDescriptorDAO cropDescriptorDao;
	
	@Before
    public void before() {
	        removeAll();
	    }

	    
	    protected void removeAll() {
	        removeAllCropData();
	    }

	    protected void removeAllCropData() {
	       

	    }

	@Test
	public void creationTest(){
		
		new CropDescriptorService();
		
		
	}
}
