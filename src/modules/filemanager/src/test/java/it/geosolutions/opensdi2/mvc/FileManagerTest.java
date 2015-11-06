/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.mvc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.geosolutions.opensdi2.configurations.configdir.OpenSDIManagerConfigImpl;
import it.geosolutions.opensdi2.configurations.controller.OSDIModuleController;
import it.geosolutions.opensdi2.configurations.dao.PropertiesDAO;
import it.geosolutions.opensdi2.utils.PropertiesDirFiltersFactory;
import it.geosolutions.opensdi2.utils.ResponseConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for FileManager
 * 
 * @author adiaz
 * @author DamianoG
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/applicationContext.xml")
public class FileManagerTest {
	
	private static Logger LOGGER = Logger.getLogger(FileManagerTest.class);
	
	
	/**
	 * Controller to be tested
	 */
	@Autowired
	FileManager fileManager;
	
	/**
	 * Random to generate file and folder names at runtime
	 */
	static Random random = new Random(); 

	/**
	 * Files to generate
	 */
	private int files = 4;

	/**
	 * Folders to generate
	 */
	private int folders = 4;
	
	
	/**
	 * Created files
	 */
	List<File> currentFiles;
	
	/**
	 * Created folders
	 */
	List<File> currentFolders;
	
	@Autowired
	OpenSDIManagerConfigImpl baseConfig;
	
	@Autowired
	PropertiesDAO daoBean;
	
	/**
	 * Initialize test folder: create files and subfolders for the tests 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ConfigurationException 
	 */
	@Before
	public void setup() throws FileNotFoundException, IOException, ConfigurationException{
	    
	    File configDirTest = TestData.file(this, "configDirTest");
	    File propertiesConfigurationsDir = configDirTest.listFiles(new CustomFileNameFilter("propertiesConfigurations"))[0];
	    File rootFileManager = TestData.file(this, "rootFileManagerTest");
	    setupConfigInstanceFile(propertiesConfigurationsDir, rootFileManager);
	    
	    baseConfig.setBaseFolder(configDirTest);
	    daoBean.setPropertiesConfigDir(propertiesConfigurationsDir);
	    
	    PropertiesDirFiltersFactory factory = new PropertiesDirFiltersFactory();
	    
            File file = null;
	    currentFiles = Arrays.asList(rootFileManager.listFiles(factory.getFilter(PropertiesDirFiltersFactory.FILTER_TYPE.ALL_FILES, null)));
	    if(currentFiles.isEmpty()){
                // create files
                for (int i = 0; i < files; i++) {
                    file = new File(rootFileManager, "file"+(i+1));
                    file.createNewFile();
                }
	    }
	    currentFolders = Arrays.asList(rootFileManager.listFiles(factory.getFilter(PropertiesDirFiltersFactory.FILTER_TYPE.ALL_DIRS, null)));
            if(currentFolders.isEmpty()){
                // create folders
                for (int i = 0; i < folders; i++) {
                    file = new File(rootFileManager, "folder"+(i+1));
                    file.mkdir();
                }
	    }
	}
	
	/**
	 * Check if the list of files is correct
	 */
	@Test
	public void testFileList(){
		HttpServletResponse response = new MockHttpServletResponse();
		HttpServletRequest request = buildMockRequest();
		Object jsonResp = fileManager.extJSbrowser(FileManager.EXTJS_FILE_LIST, null, null, null, null, request, response);
		if(jsonResp != null && jsonResp instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object> json = (Map<String, Object>) jsonResp;
			Object objCount = json.get(ResponseConstants.COUNT);
			assertTrue(objCount instanceof Integer);
			assertTrue(((Integer)objCount) == (files + folders));
		}else{
			fail("Not valid response");
		}
	}
	
	/**
	 * Check if the delete file works
	 */
	@Test
	public void testFileDeleteFiles(){
		HttpServletResponse response = new MockHttpServletResponse();
		HttpServletRequest request = buildMockRequest();
		for(File file: currentFiles){
			fileManager.extJSbrowser(FileManager.EXTJS_FILE_DELETE, null, null, null, file.getName(), request, response);
		}
		Object jsonResp = fileManager.extJSbrowser(FileManager.EXTJS_FILE_LIST, null, null, null, null, request, response);
		if(jsonResp != null && jsonResp instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object> json = (Map<String, Object>) jsonResp;
			Object objCount = json.get(ResponseConstants.COUNT);
			assertTrue(objCount instanceof Integer);
			assertTrue(((Integer)objCount) == folders);
		}else{
			fail("Not valid response");
		}
	}
	
	/**
	 * Check if the delete folder works
	 */
	@Test
	public void testFileDeleteFolder(){
		HttpServletResponse response = new MockHttpServletResponse();
		HttpServletRequest request = buildMockRequest();
		for(File file: currentFolders){
			fileManager.extJSbrowser(FileManager.EXTJS_FOLDER_DEL, file.getName(), null, null, null, request, response);
		}
		Object jsonResp = fileManager.extJSbrowser(FileManager.EXTJS_FILE_LIST, null, null, null, null, request, response);
		if(jsonResp != null && jsonResp instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object> json = (Map<String, Object>) jsonResp;
			Object objCount = json.get(ResponseConstants.COUNT);
			assertTrue(objCount instanceof Integer);
			assertTrue(((Integer)objCount) == files);
		}else{
			fail("Not valid response");
		}
	}
	
	private HttpServletRequest buildMockRequest(){
	    MockHttpServletRequest req = new MockHttpServletRequest();
	    req.setParameter(OSDIModuleController.SCOPE_ID, "filemanager");
	    req.setParameter(OSDIModuleController.INSTANCE_ID, "instance1");
	    req.setPathInfo("fileManager");
	    return req;
	}
	
	private void setupConfigInstanceFile(File propertiesConfigDir, File rootFileManagerTest) throws ConfigurationException{
	    
	    CustomFileNameFilter cfnf = new CustomFileNameFilter("mod_fileManager");
	    File f = propertiesConfigDir.listFiles(cfnf)[0];
	    
	    cfnf = new CustomFileNameFilter("config_instance1.properties");
	    PropertiesConfiguration config = new PropertiesConfiguration(f.listFiles(cfnf)[0]);
	    config.setProperty("rootDir", rootFileManagerTest.getAbsolutePath());
	    config.save();
	}
	
	public class CustomFileNameFilter implements FilenameFilter{

            private String nameToCheck;
            
            public CustomFileNameFilter(String name){
                this.nameToCheck = name;
            }
            
            @Override
            public boolean accept(File dir, String name) {
                if(name!=null && nameToCheck !=null && name.equals(nameToCheck)){
                    return true;
                }
                return false;
            }
        }

}
