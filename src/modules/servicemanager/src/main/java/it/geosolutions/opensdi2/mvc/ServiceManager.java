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

import it.geosolutions.httpproxy.callback.ProxyCallback;
import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.service.ProxyService;
import it.geosolutions.httpproxy.utils.ProxyInfo;
import it.geosolutions.opensdi2.config.FileManagerConfig;
import it.geosolutions.opensdi2.config.FolderPermission;
import it.geosolutions.opensdi2.utils.ResponseConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletResponse;
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
	
	private static String CONFIRMED_AOI_NAME = "AOI.zip";
	
	private ProxyService proxyService;

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
	
	ProxyCallback callback;

	/**
	 * @param proxyService the proxyService to set
	 */
	@Resource(name = "serviceManagerProxy")
	public void setProxyService(ProxyService proxyService) {
		 	callback = new ProxyCallback() {
			 	String user;
				String service;
				
				@Override
				public void setProxyConfig(ProxyConfig config) {
				}
				
				@Override
				public void onRequest(HttpServletRequest request,
						HttpServletResponse response, URL url) throws IOException {
					service = request.getParameter("service");
					user = request.getParameter("user");
					
				}
				
				@Override
				public void onRemoteResponse(HttpMethod method) throws IOException {
					String filePath = fileManagerConfig.getBaseFolder() + File.separator + user + File.separator + service + File.separator + CONFIRMED_AOI_NAME;
					if(LOGGER.isInfoEnabled())
						LOGGER.info("Download confirmed AOI to "+ filePath);
					InputStream is = method.getResponseBodyAsStream();
					byte[] bytes = IOUtils.toByteArray(is);
					createCommittedFile(filePath, bytes);	
				}
	
				/**
				 * Create committed file with byte array with a byte array
				 * 
				 * @param filePath of the file
				 * @param bytes to write
				 * @return absolute path to the file
				 * @throws IOException
				 */
				private String createCommittedFile(String filePath, byte[] bytes)
				        throws IOException {
				
				    try {
				
				        // write bytes
				        File tmpFile = new File(filePath);
				        
				        if(LOGGER.isTraceEnabled()){
				            LOGGER.trace("Appending bytes to " + tmpFile.getAbsolutePath());
				        }
				        
				        // File channel to append bytes
				        @SuppressWarnings("resource")
						FileChannel channel = new FileOutputStream(tmpFile, true).getChannel();
				        ByteBuffer buf = ByteBuffer.allocateDirect((int)bytes.length);
				        
				        // put bytes
				        buf.put(bytes);
				        
				        // Flips this buffer.  The limit is set to the current position and then
				        // the position is set to zero.  If the mark is defined then it is discarded.
				        buf.flip();
				        
				        // Writes a sequence of bytes to this channel from the given buffer.
				        channel.write(buf);
				     
				        // close the channel
				        channel.close();
				        
				    } catch (IOException e) {
				        LOGGER.error("Error writing file bytes", e);
				    }
					
				    return filePath;
				}
				
				@Override
				public void onFinish() throws IOException {
				}
				
				@Override
				public boolean beforeExecuteProxyRequest(HttpMethod httpMethodProxyRequest,
						HttpServletRequest httpServletRequest,
						HttpServletResponse httpServletResponse, String user,
						String password, ProxyInfo proxyInfo) {
					return true;
				}
		};
		proxyService.addCallback(callback);
		this.proxyService = proxyService;
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
	 * Download a confirmed AOI to target folder
	 * 
	 * @param user
	 * @param service
	 * @param request
	 * @param httpServletResponse
	 * @return
	 */
	@RequestMapping(value = "confirmServiceAOI", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	Object confirmService(
			@RequestParam(value = "user", required = true) String user,
			@RequestParam(value = "service", required = true) String service,
			HttpServletRequest request, HttpServletResponse httpServletResponse) {

		// TODO: Check user and service
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			proxyService.execute(request, new MockHttpServletResponse());
			response.put(ResponseConstants.SUCCESS, true);
		} catch (Exception e) {
			LOGGER.error("Error downloading finished AOI", e);
			response.put(ResponseConstants.SUCCESS, false);
		}
		return response;
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

	/**
	 * @return the proxyService
	 */
	public ProxyService getProxyService() {
		return proxyService;
	}
}
