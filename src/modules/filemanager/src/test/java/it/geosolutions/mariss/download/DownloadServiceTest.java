package it.geosolutions.mariss.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
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
@ContextConfiguration(locations = "classpath:/downloadContext-test.xml")
public class DownloadServiceTest {
	private static Logger LOGGER = Logger.getLogger(DownloadServiceTest.class);
	private static final String SERVICEID = "service";
	private static final String PRODUCTID = "product";
	private static final String IDELIMITER = "_I_";
	private static final String SDELIMITER ="_s_";
	private static final String FILE_PRE = IDELIMITER + PRODUCTID + IDELIMITER +
			SDELIMITER + SERVICEID + SDELIMITER;
	private static final String FILE_NAME = "test_file.txt";
	private String defaultResourceBasePath = "/"
            + DownloadServiceTest.class.getPackage().getName()
                    .replace(".", "/");
	@Autowired
	MarissDownloadService downloadService;
	@Test
	public void downloadTest() {
		PipedInputStream pin = new PipedInputStream();
		PipedOutputStream pout;
		try {
			pout = new PipedOutputStream(pin);

			MarissOrder order = new MarissOrder();
			ArrayList<String> list = new ArrayList<String>();
			URL url =getClass().getResource(defaultResourceBasePath + "/" + FILE_PRE + FILE_NAME);
			assertTrue(url != null);
			File f = null;
			try {
				f = new File(url.toURI());
			} catch (URISyntaxException e) {
				fail(e.getMessage());
			}
			DownloadTestUtils.addToOrder(SERVICEID, PRODUCTID,f.getAbsolutePath() , order);
			
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
			if(entry.getName().equals(SERVICEID + "/" + PRODUCTID + "/"+ FILE_NAME)){
				
				while (zis.available() > 0){
					zis.read(buffer);
				}
				String b = new String(buffer);
				LOGGER.info("content: " + b);
				assertEquals(test,b);
				textFileCheck =true;
			}else{
				fail();
			}
		}
		assertTrue(textFileCheck);
	}

}
