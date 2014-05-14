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

import it.geosolutions.opensdi2.config.FileManagerConfig;
import it.geosolutions.opensdi2.config.FolderPermission;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for the service file manager.
 * 
 * @author Alejandro Diaz (alejandro.diaz at geo-solutions.it)
 * 
 */
@Controller
@RequestMapping("/serviceManager")
public class ServiceManager extends BaseFileManager {

	/**
	 * Base configuration for the service manager
	 */
	private FileManagerConfig fileManagerConfig;

	/**
	 * Set the configuration to set up the base directory
	 * 
	 * @param config
	 */
	@Resource(name = "serviceManagerConfig")
	public void setBaseConfig(FileManagerConfig baseConfig) {
		this.fileManagerConfig = baseConfig;
		this.setRuntimeDir(baseConfig.getBaseFolder());
	}

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
	@RequestMapping(value = "extJSbrowser", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object extJSbrowser(
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "oldName", required = false) String oldName,
			@RequestParam(value = "file", required = false) String file,
			HttpServletRequest request, HttpServletResponse response) {

		if (EXTJS_FILE_DOWNLOAD.equals(action)) {
			String finalFolder = folder != null && !folder.equals("root") ? folder
					: null;
			if (finalFolder != null) {
				if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
					finalFolder = finalFolder.replace(
							fileManagerConfig.getRootText(), "");
				}
			}
			String finalFile = file;
			if (finalFile != null) {
				if (finalFile.startsWith(fileManagerConfig.getRootText())) {
					finalFile = finalFile.replace(
							fileManagerConfig.getRootText(), "");
				}
			}
			download(response, finalFile, getFilePath(finalFile, finalFolder));
			return null;
		} else {
			return super.extJSbrowser(action, folder, name, oldName, file,
					request, response);
		}
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
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(
			@RequestParam MultipartFile file,
			@RequestParam(required = false, defaultValue = "uploadedFile") String name,
			@RequestParam(required = false, defaultValue = "-1") int chunks,
			@RequestParam(required = false, defaultValue = "-1") int chunk,
			@RequestParam(required = false) String folder,
			HttpServletRequest request, HttpServletResponse servletResponse)
			throws IOException {
		String finalFolder = folder;
		if (finalFolder != null) {
			if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
				finalFolder = finalFolder.replace(
						fileManagerConfig.getRootText(), "");
			}
		}
		super.upload(file, name, chunks, chunk, finalFolder, request,
				servletResponse);
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
	@RequestMapping(value = "download", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void downloadFile(
			@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "file", required = true) String file,
			HttpServletResponse resp) {
		String finalFolder = folder;
		if (finalFolder != null) {
			if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
				finalFolder = finalFolder.replace(
						fileManagerConfig.getRootText(), "");
			}
		}
		String finalFile = file;
		if (finalFile != null) {
			if (finalFile.startsWith(fileManagerConfig.getRootText())) {
				finalFile = finalFile.replace(fileManagerConfig.getRootText(),
						"");
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Download file "
					+ ((finalFolder != null) ? finalFolder + finalFile
							: finalFile));
		}
		super.downloadFile(finalFolder, finalFile, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.geosolutions.opensdi2.mvc.BaseFileManager#getFolderList(java.lang.
	 * String)
	 */
	@Override
	protected List<Map<String, Object>> getFolderList(String folder) {
		List<Map<String, Object>> currenteFolderList = super
				.getFolderList(folder);

		FolderPermission permission = fileManagerConfig.getPermission(folder);

		// write operations available
		for (Map<String, Object> rootElement : currenteFolderList) {
			rootElement.put("canRename", permission.canRename());
			rootElement.put("canDelete", permission.canDelete());
			rootElement.put("canCreateFolder", permission.canCreateFolder());
			rootElement.put("canUpload", permission.canUpload());
		}

		return currenteFolderList;
	}
}
