package it.geosolutions.npa;

import it.geosolutions.npa.model.RoleUSIDRule;
import it.geosolutions.npa.service.impl.JDBCUSIDService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class DummyUsidService extends JDBCUSIDService{
	private static Logger LOGGER = Logger.getLogger(DummyUsidService.class);
	 // refactoring-proof way of setting the base path for lookup of config files
    // templates
    private String defaultResourceBasePath = "/"
            + DummyUsidService.class.getPackage().getName()
                    .replace(".", "/");
	
	Collection<RoleUSIDRule> rules = new HashSet<RoleUSIDRule>();
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@Override
	public int countAll() throws IOException {
		return rules.size();
	}

	@Override
	public Collection<RoleUSIDRule> getAllRecords(int pageSize, int offset)
			throws IOException {
		return rules;
	}

	@Override
	public RoleUSIDRule getSingleRecord(String role, Object usid)
			throws IOException {
		return new RoleUSIDRule(role, (String) usid);
	}

	@Override
	public int countUsidForRole(String role) throws IOException {
		// TODO NOT IMPLEMENTEDs
		return 0;
	}

	@Override
	public List<Object> getUsidForRole(String role) throws IOException {
		HashSet<Object> match = new HashSet<Object>();
		for(RoleUSIDRule rule : rules){
			if(role.equals(rule.getRole())){
				match.add(rule.getUsid());
			}
		}
		return new ArrayList<Object>(match);
		
	}

	@Override
	public List<Object> getUsidForRole(String role, int pageSize,
			int offset) throws IOException {
		//PAGINATION NOT IMPLEMENTED
		HashSet<Object> match = new HashSet<Object>();
		for(RoleUSIDRule rule : rules){
			if(role.equals(rule.getRole())){
				match.add(rule.getUsid());
			}
		}
		return new ArrayList<Object>(match);
	}

	@Override
	public int countRolesForUsid(Object usid) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<String> getRolesForUsid(Object usid) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getRolesForUsid(Object usid, int pageSize,
			int offset) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addUsidForRole(String role, Object usid) throws IOException {
		rules.add(new RoleUSIDRule(role, (String) usid));
		return true;
	}

	@Override
	public boolean deleteUsidForRole(String role, Object usid)
			throws IOException {
		 return rules.remove(new RoleUSIDRule(role, (String) usid));
		
	}
	@Override
    public void afterPropertiesSet() throws Exception {
		// NOTE : these operations are here to create
		// the test environment before the bean initialization
		// the test folder is created here so also download 
		// creation is here. (create a reference and put these methods
		// in the test in the future) 
		File tempFolder = testFolder.newFolder("npa_test");
		setBaseDirectory(tempFolder);
		setDmlFileName("test-dml.xml");
		setDdlFileName("test-ddl.xml");
		tempFolder.deleteOnExit();
		//create also file for download test
		File downloadDir = new File(tempFolder,"download");
		downloadDir.mkdir();
		File dir1 = new File(downloadDir,"1010");
		File dir2 =new File(downloadDir,"1011");
		dir1.mkdir();
		dir2.mkdir();
		
		
		//create a not empty file
		URL url =getClass().getResource(defaultResourceBasePath + "/" + "test_file.txt");
		FileUtils.copyURLToFile(url, new File(downloadDir,"1010_test_file.txt" ) );
		
		//create test zip file
		File testFile1 = new File(dir1,"1010_test.zip");
		FileOutputStream fos = new FileOutputStream(testFile1);
		ZipOutputStream zos = new ZipOutputStream(fos);
		//fill the zip file
		addToZipFile(new File(downloadDir,"1010_test_file.txt" ), zos);
		
		//copy test configurations for tests
		FileUtils.copyURLToFile(getClass().getResource(defaultResourceBasePath + "/" + "jdbc.xml"),
				new File(getConfigRoot(),getJdbcConfigFileName() ) );
		FileUtils.copyURLToFile(getClass().getResource(defaultResourceBasePath + "/" + "test-ddl.xml"),
				new File(getConfigRoot(), "test-ddl.xml") );
		FileUtils.copyURLToFile(getClass().getResource(defaultResourceBasePath + "/" + "test-dml.xml"),
				new File(getConfigRoot(), "test-dml.xml") );
		FileUtils.copyURLToFile(getClass().getResource(defaultResourceBasePath + "/" + "download.xml"),
				new File(getConfigRoot(), "download.xml") );
		
		super.afterPropertiesSet();
    }
	public static void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {

		LOGGER.info("Writing '" + file.getName() + "' to zip file");

		
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}
		zos.flush();
		zos.closeEntry();
		fis.close();
		
	}
	

}
