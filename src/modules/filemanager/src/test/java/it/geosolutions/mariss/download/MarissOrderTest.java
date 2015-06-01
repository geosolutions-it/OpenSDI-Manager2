package it.geosolutions.mariss.download;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class MarissOrderTest {
/*
 * "test_alfa001": {
        "0009_CSKS2_DGM_B_HI_16_HH_RD_FF_20110924180409_20110924180416": [
            "/share/gs_ext_data/data/netcdfdatadir/dirmet/_I_0009_CSKS2_DGM_B_HI_16_HH_RD_FF_20110924180409_20110924180416_I__s_test_alfa001_s_SAR_wave_Dim_partition_DimVal_0_DimEnd__Var_DIRMET.nc"
        ],

 */
	@Test
	public void testMarissOrder(){
		
		final String serviceId = "test_alfa001";
		final String productId = "0009_CSKS2_DGM_B_HI_16_HH_RD_FF_20110924180409_20110924180416"; 
		final String lastPart = "SAR_wave_Dim_partition_DimVal_0_DimEnd__Var_DIRMET.nc";
		final String fileName1 = "/share/gs_ext_data/data/netcdfdatadir/dirmet/_I_0009_CSKS2_DGM_B_HI_16_HH_RD_FF_20110924180409_20110924180416_I__s_test_alfa001_s_"+ lastPart;
		MarissOrder order = new MarissOrder();
		DownloadTestUtils.addToOrder(serviceId, productId, fileName1, order);
		Map<String,String> filesToCreate = order.getFilesToCreate();
		assertTrue(filesToCreate.size() == 1);
		for( String name : filesToCreate.keySet()) {
			String createdFileName = FilenameUtils.getName(name);
			assertEquals(createdFileName, lastPart);
		}
		
	}

	
}
