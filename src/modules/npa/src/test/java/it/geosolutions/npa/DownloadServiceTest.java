package it.geosolutions.npa;

import static org.junit.Assert.*;
import it.geosolutions.npa.download.NPADownloadService;
import it.geosolutions.npa.service.USIDService;
import it.geosolutions.opensdi2.download.order.ListOrder;
import it.geosolutions.opensdi2.ftp.user.AuthoritiesProvider;
import it.geosolutions.opensdi2.ftp.user.geostore.GeoStoreFTPUser;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * NPA Portal test for download service
 * 
 * @author Lorenzo Natali
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext-test.xml")
public class DownloadServiceTest {
	private static Logger LOGGER = Logger.getLogger(DownloadServiceTest.class);

	@Autowired
	AuthoritiesProvider authoritiesProvider;

	@Autowired
	USIDService usidService;

	@Autowired
	NPADownloadService downloadService;

	@Autowired
	GeoStoreFTPUser dummyUser;

	@Test
	public void downloadTest() {
		PipedInputStream pin = new PipedInputStream();
		PipedOutputStream pout;
		try {
			pout = new PipedOutputStream(pin);

			ListOrder order = new ListOrder();
			ArrayList<String> list = new ArrayList<String>();
			list.add("1010");
			order.setOrder(list);
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(pin));
			ZipInputStream zis = new ZipInputStream(pin);
			// this is where you start, with an InputStream containing the bytes
			// from the zip file
			downloadService.getDownload(order, pout);
			checkZipFile(zis);
		} catch (IOException e) {
			fail();
		}

	}

	private void checkZipFile(ZipInputStream zis) throws IOException {
		ZipEntry entry;
		String test = "this is a test file";
		byte[] buffer = new byte[test.length()] ;
		boolean textFileCheck = false;
		while ((entry = zis.getNextEntry()) != null) {
			LOGGER.info("entry: " + entry.getName() + ", " + entry.getSize());
			//test the file content
			if(entry.getName().equals("1010_test_file.txt")){
				
				while (zis.available() > 0){
					zis.read(buffer);
				}
				String b = new String(buffer);
				LOGGER.info("content: " + b);
				assertEquals(test,b);
				textFileCheck =true;
			}
		}
		assertTrue(textFileCheck);
	}

}
