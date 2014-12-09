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

import it.geosolutions.geocollect.model.http.CommitResponse;
import it.geosolutions.geocollect.model.http.Status;
import it.geosolutions.opensdi2.config.OpenSDIManagerConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller data for media and files for Geocollect.
 * This provides functionalities of the typical Extjs File Browser, and some utility 
 * methods to keep updated a data structure for GeoCollect application Contents.
 * 
 * **NOTE** : This have to be hard-coded because of this https://jira.spring.io/browse/SPR-5757
 * Once the issue is resolved and Spring Updated, use xml configuration to map base
 * class.
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
@Controller
@RequestMapping("/geocollect/data")
@PreAuthorize("!hasRole('ROLE_ANONYMOUS')")
public class GeoCollectDataController extends BaseFileManager {
	
	private static final String GC_BASE_DIR = "geocollect";
	private static final String GC_MEDIA_DIR = "media";
	private static final String GC_CONFIG_DIR = "config";

	/**
	 * Upload a media content in the upload folder
	 * @param file the file to upload
	 * @param mission the mission folder
	 * @param source a source of the data committed
	 * @param id the identifier of the origin of this mission
	 * @param name the file name
	 * @param request original servlet request
	 * @param servletResponse original servlet response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/{mission}/{source}/{id}/upload",method={RequestMethod.POST})
	public @ResponseBody CommitResponse upload(
			@RequestParam MultipartFile file,
			@PathVariable("mission") String mission,
			@PathVariable("source") String source,
			@PathVariable("id") String id,
			@RequestParam(value = "name",required = false, defaultValue = "uploadedFile") String name,
			HttpServletRequest request, HttpServletResponse servletResponse)
					throws IOException {
		String folder = generateFolder(mission,source,id);
		//note now it doesn't support chunked requests
		super.upload(file, name, -1, -1, folder, request, servletResponse);
		CommitResponse cr =  new CommitResponse();
		cr.setStatus(Status.SUCCESS);
		return cr;
	}
	
	@RequestMapping(value="/{mission}/{source}/media")
	public @ResponseBody Object getMedia(
			@RequestParam(value = "file", required = false) String file,
			@PathVariable("mission") String mission,
			@PathVariable("source") String source,
			HttpServletRequest request, HttpServletResponse response){
		
		String sourcePath = getGeoCollectPath(GC_MEDIA_DIR,mission, source, null);
		Map<String, Object> folders = getFileList(sourcePath);
		Map<String,Object> result = new HashMap<String,Object>();
		//TODO make it recursive to explore the whole files
		for (String folderName: folders.keySet()){
			if(folders.get(folderName) instanceof Map){
				Map<String, Object> objectData = (Map<String, Object>) folders.get(folderName);
				if( "folder".equals(objectData.get("iconCls") )){//TODO do a better check
					Map<String, Object> detailobj = getFileList(sourcePath + objectData.get("name"));
					
					result.putAll(getImages(detailobj));
				}else{
					result.put(folderName, objectData);
				}
			}
		}
		return result;
	}
	private Map<? extends String, ? extends Object> getImages(
			Map<String, Object> detailobj) {
		 Map<String,Object> res = new HashMap<String,Object>();
		 for(String key : detailobj.keySet()){
			 Map<String,Object> current = (Map<String,Object>) detailobj.get(key);
			 if(isImage(current)){
				 res.put(key,current);
			 }
		 }
		 return res;
	}
	/**
	 * Check type from the object descriptor
	 * @param current
	 * @return
	 */
	private boolean isImage(Map<String,Object> current) {
		String name  =(String) current.get("name");
		return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif");
		
	}

	/**
	 * Generate the folder for the file to upload (in the runtimedir under geocollect directory
	 * @param mission the mission
	 * @param source
	 * @param id
	 * @return
	 */
	private String generateFolder(String mission, String source, String id) {
		String rel_path = getGeoCollectPath(GC_MEDIA_DIR,mission, source, id);
		String path = getFilePath("",rel_path);
		new File(path).mkdirs();
		return rel_path;
	}
	/**
	 * Return the path for a file to upload
	 * @param subdir the main directory (data, config...)
	 * @param mission the mission name
	 * @param source the origin name
	 * @param id the id of the commit
	 * @return the path to the file
	 */
	private String getGeoCollectPath(String subdir, String mission, String source, String id) {
		String rel_path =  GC_BASE_DIR + File.separator +
				subdir + File.separator +
				mission + File.separator + source + File.separator ;
		if(id!=null){
			rel_path += id + File.separator;
		}
		return rel_path;
	}


	/**
	 * Set the configuration to set up the base directory
	 * @param config
	 */
	@Autowired
	public void setBaseConfig(OpenSDIManagerConfig baseConfig){
		
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

		return super.extJSbrowser(action, folder, name, oldName, file, request, response);

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

		super.upload(file, name, chunks, chunk, folder, request, servletResponse);
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
			HttpServletResponse resp) {
		super.downloadFile(folder, file, resp);
	}

	@Override
	protected List<Object> getActions(File file) {
		// TODO Auto-generated method stub
		return super.getActions(file);
	}
}