/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 - 2016 GeoSolutions S.A.S.
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

import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for the base file manager.
 * **NOTE** : This have to be hard-coded because of this https://jira.spring.io/browse/SPR-5757
 * Once the issue is resolved and Spring Updated, use xml configuration to map base
 * class.
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
@Controller
@RequestMapping("/admin/updater/")
public class UpdaterFileManager extends BaseFileManager {

	public static final String ROOT_DIR = "rootDir";
    
	/**
	 * Browser handler server side for ExtJS filebrowser.
	 * 
	 * @see https://code.google.com/p/ext-ux-filebrowserpanel/
	 * 
	 * @param action
	 *            to perform
	 * @param folder
	 *            folder to browse
	 * @param file
	 *            to perform an operation
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * 
	 * @return
	 */
	@RequestMapping(value = "/extJSbrowser", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object extJSbrowser(
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "oldName", required = false) String oldName,
			@RequestParam(value = "file", required = false) String file,
			HttpServletRequest request, HttpServletResponse response) {

		String rootDir = configureModule(request);
		return super.extJSbrowser(rootDir, action, folder, name, oldName, file, request, response);

	}

	/**
	 * Handler for upload files
	 * 
	 * @param operationId
	 * @param gotHeaders
	 * @param file
	 *            uploaded
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(
			@RequestParam MultipartFile file,
			@RequestParam(required = false, defaultValue = "uploadedFile") String name,
			@RequestParam(required = false, defaultValue = "-1") int chunks,
			@RequestParam(required = false, defaultValue = "-1") int chunk,
			@RequestParam(required = false) String folder,
			HttpServletRequest request, HttpServletResponse servletResponse)
			throws IOException {

		String rootDir = configureModule(request);
		super.upload(rootDir, file, name, chunks, chunk, folder, request, servletResponse);
	}


	/**
	 * Download a file
	 * 
	 * @param folder
	 *            folder for the file
	 * @param file
	 *            to be downloaded
	 * @param resp
	 *            servlet response
	 */
	@RequestMapping(value = "/download", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void downloadFile(
			@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "file", required = true) String file,
			HttpServletRequest req,
			HttpServletResponse resp) {
		String rootDir = configureModule(req);
		super.downloadFile(rootDir, folder, file, resp);
	}
	
	@Override
	protected List<Object> getActions(File file) {
		// TODO Auto-generated method stub
		return super.getActions(file);
	}
	
	/**
	 * This Method is responsible for retrieve the configuration object from the request, search for the runtimeDir parameter and set
	 * the related superclass instance variable
	 * 
	 * @param request
	 */
	private String configureModule(HttpServletRequest request){
	    
	    OSDIConfigurationKVP config;
            try {
                config = (OSDIConfigurationKVP) super.loadConfiguration(request);
            } catch (OSDIConfigurationException e) {
                LOGGER.error(e.getMessage(), e);
                throw new IllegalStateException("The configuration for the module '" + FileManager.class + "' cannot be load...");
            }
	    String rootDir = (String)config.getValue(ROOT_DIR);
	    if(StringUtils.isBlank(rootDir)){
	        throw new IllegalStateException("The module configuration provided has an empty 'runtimeDir' value");
	    }
	    return rootDir;
	}
}
