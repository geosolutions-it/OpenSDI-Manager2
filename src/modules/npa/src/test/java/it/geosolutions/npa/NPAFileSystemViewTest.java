package it.geosolutions.npa;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.npa.download.NPADownloadService;
import it.geosolutions.npa.ftp.NPAFileSystemView;
import it.geosolutions.npa.service.USIDService;
import it.geosolutions.opensdi2.ftp.user.AuthoritiesProvider;
import it.geosolutions.opensdi2.ftp.user.geostore.GeoStoreFTPUser;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/applicationContext-test.xml")
public class NPAFileSystemViewTest {
	
	private final static Logger LOGGER = Logger
			.getLogger(NPAFileSystemViewTest.class);
	
	@Autowired
	AuthoritiesProvider authoritiesProvider;
	
	@Autowired
	USIDService usidService;
	
	@Autowired
	NPADownloadService downloadService;
	
	@Autowired
	GeoStoreFTPUser dummyUser;
	
	@Test
	public void testUtilityMethods(){
		//path normalization
		assertEquals("/",NPAFileSystemView.getParentDirPath("/"));
		assertEquals("/",NPAFileSystemView.getParentDirPath("/a"));
		assertEquals("",NPAFileSystemView.getParentDirPath("a"));
		assertEquals("/a",NPAFileSystemView.getParentDirPath("/a/b"));
		assertEquals("/a",NPAFileSystemView.getParentDirPath("/a/b/"));
		assertEquals("/a/b",NPAFileSystemView.getParentDirPath("/a/b/c"));
		
		
		
		//change directory
		try {
			usidService.addUsidForRole("everyone", "1010");
			NPAFileSystemView view = new NPAFileSystemView(dummyUser, usidService, downloadService);
			//current directory / 
			assertTrue(view.changeWorkingDirectory("/"));
			
			//absolute path
			//current directory /1010
			assertTrue(view.changeWorkingDirectory("/1010"));
			
			//current dir
			FtpFile ftpDir = view.getFile("./");
			assertNotNull(ftpDir);
			assertEquals("/1010", ftpDir.getAbsolutePath());
			assertTrue(ftpDir.isDirectory());
			
			checkDirectory(view, ftpDir);
			
			//current directory /
			assertTrue(view.changeWorkingDirectory(".."));
			
			//relative path
			assertNotNull(view.getFile("1010"));
			//current directory /1010
			assertTrue(view.changeWorkingDirectory("1010"));
			checkDirectory(view, view.getFile("./"));
			checkDirectory(view, view.getFile("/"));
			checkDirectory(view, view.getFile("/1010"));
			
			//getFile
			FtpFile zip = view.getFile("/1010/1010_test.zip");
			assertTrue(zip.getSize()>0);
			InputStream is = zip.createInputStream(0);
			assertNotNull(is);
			checkZipFile(is);
			
		} catch (FtpException e) {
			
			fail(e.getMessage());
		} catch (IOException e) {
			
			fail(e.getMessage());
		} catch (Exception e){
			fail(e.getMessage());
		}
		

	}

	/**
	 * Iterates a directory and check there are files inside
	 * @param view
	 * @param ftpDir
	 * @throws FtpException
	 */
	private void checkDirectory(NPAFileSystemView view, FtpFile ftpDir)
			throws FtpException {
		//list files
		List<FtpFile> files = ftpDir.listFiles();
		assertTrue(files.size()>0);
		for(FtpFile ftpfile:files){
			if(ftpfile.isFile()){
				String name = ftpfile.getName();
				assertNotNull(view.getFile(name));
				String absPath = ftpDir.getAbsolutePath();
				assertEquals(ftpfile.getAbsolutePath(), (absPath.equals("/")? "":absPath) + "/" + name);
			}
		}
	}
	
	public void checkZipFile(InputStream is) throws Exception{
		ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
            // while there are entries I process them
        while ((entry = zis.getNextEntry()) != null)
        {
            LOGGER.debug("entry: " + entry.getName() + ", " + entry.getSize());
                    // consume all the data from this entry
            while (zis.available() > 0)
                zis.read();
                    
        }
	}
}
