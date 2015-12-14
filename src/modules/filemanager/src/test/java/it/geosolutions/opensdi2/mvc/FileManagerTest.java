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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.geosolutions.opensdi2.config.OpenSDIManagerConfigImpl;
import it.geosolutions.opensdi2.utils.ControllerUtils;
import it.geosolutions.opensdi2.utils.ResponseConstants;

/**
 * Test class for FileManager
 * 
 * @author adiaz
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext.xml")
@WebAppConfiguration
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
    private int files = 10;

    /**
     * Folders to generate
     */
    private int folders = 10;

    /**
     * Base path for the test
     */
    String basePath;

    /**
     * Created files
     */
    List<File> currentFiles;

    /**
     * Created folders
     */
    List<File> currentFolders;

    /**
     * Initialize test folder: create files and subfolders for the tests
     */
    @Before
    public void setup() {
        try {
            // Clear lists
            currentFiles = new LinkedList<File>();
            currentFolders = new LinkedList<File>();
            // folder is generated for the test
            basePath = System.getProperty("java.io.tmpdir") + ControllerUtils.SEPARATOR
                    + random.nextInt();
            OpenSDIManagerConfigImpl config = new OpenSDIManagerConfigImpl();
            config.setBaseFolder(basePath + ControllerUtils.SEPARATOR);
            fileManager.setRuntimeDir(config.getBaseFolder());
            // create test folder
            File file = new File(basePath);
            file.mkdir();
            // create files
            for (int i = 0; i < files; i++) {
                file = new File(basePath + ControllerUtils.SEPARATOR + random.nextInt() + ".txt");
                file.createNewFile();
                currentFiles.add(file);
            }
            // create folders
            for (int i = 0; i < folders; i++) {
                file = new File(basePath + ControllerUtils.SEPARATOR + random.nextInt());
                file.mkdir();
                currentFolders.add(file);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Check if the list of files is correct
     */
    @Test
    public void testFileList() {
        HttpServletResponse response = new MockHttpServletResponse();
        Object jsonResp;
        try {
            jsonResp = fileManager.extJSbrowser(BaseFileManager.EXTJS_FILE_LIST, null, null, null,
                    null, new MockHttpServletRequest(), response);
            if (jsonResp != null && jsonResp instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> json = (Map<String, Object>) jsonResp;
                Object objCount = json.get(ResponseConstants.COUNT);
                assertTrue(objCount instanceof Integer);
                assertTrue(((Integer) objCount) == (files + folders));
            } else {
                fail("Not valid response");
            }
        } catch (Exception e) {
            fail("Not valid response");
        }
    }

    /**
     * Check if the delete file works
     */
    @Test
    public void testFileDeleteFiles() {
        HttpServletResponse response = new MockHttpServletResponse();
        for (File file : currentFiles) {
            try {
                fileManager.extJSbrowser(BaseFileManager.EXTJS_FILE_DELETE, null, file.getName(), null,
                        null, new MockHttpServletRequest(), response);
            } catch (Exception e) {
                fail("Not valid response");
            }
        }
        
        Object jsonResp;
        try {
            jsonResp = fileManager.extJSbrowser(BaseFileManager.EXTJS_FILE_LIST, null, null, null,
                    null, new MockHttpServletRequest(), response);
            if (jsonResp != null && jsonResp instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> json = (Map<String, Object>) jsonResp;
                Object objCount = json.get(ResponseConstants.COUNT);
                assertTrue(objCount instanceof Integer);
                assertTrue(((Integer) objCount) == folders);
            } else {
                fail("Not valid response");
            }
        } catch (Exception e) {
            fail("Not valid response");
        }
    }

    /**
     * Check if the delete folder works
     */
    @Test
    public void testFileDeleteFolder() {
        HttpServletResponse response = new MockHttpServletResponse();
        for (File file : currentFolders) {
            try {
                fileManager.extJSbrowser(BaseFileManager.EXTJS_FOLDER_DEL, file.getName(), null, null, null,
                        new MockHttpServletRequest(), response);
            } catch (Exception e) {
                fail("Not valid response");
            }
        }
        Object jsonResp;
        try {
            jsonResp = fileManager.extJSbrowser(BaseFileManager.EXTJS_FILE_LIST, null, null, null,
                    null, new MockHttpServletRequest(), response);
            if (jsonResp != null && jsonResp instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> json = (Map<String, Object>) jsonResp;
                Object objCount = json.get(ResponseConstants.COUNT);
                assertTrue(objCount instanceof Integer);
                assertTrue(((Integer) objCount) == files);
            } else {
                fail("Not valid response");
            }
        } catch (Exception e) {
            fail("Not valid response");
        }
    }

    /**
     * Remove test folder
     * 
     * @throws IOException on folder delete
     */
    @After
    public void cleanup() throws IOException {
        File file = new File(basePath);
        FileUtils.deleteDirectory(file);
    }

}
