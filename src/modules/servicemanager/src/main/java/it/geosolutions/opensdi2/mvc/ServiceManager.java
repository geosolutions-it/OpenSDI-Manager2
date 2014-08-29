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

import it.geosolutions.geobatch.mariss.dao.ServiceDAO;
import it.geosolutions.geobatch.mariss.model.AreaOfInterest;
import it.geosolutions.geobatch.mariss.model.Sensor;
import it.geosolutions.geobatch.mariss.model.SensorMode;
import it.geosolutions.geobatch.mariss.model.Service;
import it.geosolutions.httpproxy.callback.ProxyCallback;
import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.service.ProxyService;
import it.geosolutions.httpproxy.utils.ProxyInfo;
import it.geosolutions.opensdi2.config.FileManagerConfig;
import it.geosolutions.opensdi2.config.FolderPermission;
import it.geosolutions.opensdi2.utils.ControllerUtils;
import it.geosolutions.opensdi2.utils.ResponseConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * Spring JDBC DAOs
     */
    private ServiceDAO serviceDAO;

    /**
     * Confirmed AOI names
     */
    private static String CONFIRMED_AOI_NAME = "AOI.zip";

    /**
     * Confirmed ACQ_PLAN names
     */
    private static String CONFIRMED_ACQ_PLAN = "ACQ_PLAN.zip";

    private static String CONFIRMED_ACQ_PLAN_NAME = "ACQ_PLAN.xml";

    /**
     * METHODS to change service status
     */
    private static String METHOD_CONFIRMED_ACQ_PLAN = "ACQ_PLAN";

    private static String METHOD_CONFIRMED_AOI = "METHOD_CONFIRMED_AOI";

    private final String ACQ_LIST_FOLDER = "ACQ_LIST";

    private final String PRODUCTS_FOLDER = "PRODUCTS";
    
    /**
     * Known operation: Extjs integration services list
     */
    public static final String EXTJS_SERVICES_LIST = "get_serviceslist";

    /**
     * Known operation: Extjs integration sensors list
     */
    public static final String EXTJS_SENSORS_LIST = "get_sensorslist";

    /**
     * Known operation: Extjs integration sensorModes list
     */
    public static final String EXTJS_SENSOR_MODES_LIST = "get_sensorModeslist";
    
    /**
     * Known operation: Extjs integration serviceSensors list
     */
    public static final String EXTJS_SERVICE_SENSORS_LIST = "get_servicesensorslist";
    
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

    /**
     * @param serviceDAO the serviceDAO to set
     */
    @Resource(name = "osdi2ServiceDAO")
    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    /**
     * @return the serviceDAO
     */
    public ServiceDAO getServiceDAO() {
        return serviceDAO;
    }

    ProxyCallback callback;

    String downloadMethod;

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

            /**
             * On request we save current call
             */
            public void onRequest(HttpServletRequest request, HttpServletResponse response, URL url)
                    throws IOException {
                service = request.getParameter("service");
                user = request.getParameter("user");
            }

            /**
             * On remote response we download the files and manipulate it
             */
            public void onRemoteResponse(HttpMethod method) throws IOException {
                if (METHOD_CONFIRMED_ACQ_PLAN.endsWith(downloadMethod)) {
                    downloadConfirmedAcqPLan(method);
                } else {
                    downloadConfirmedAOI(method);
                }
            }

            /**
             * Download the shape-zip for the AOI of the plan
             * 
             * @param method
             * @throws IOException
             */
            private void downloadConfirmedAOI(HttpMethod method) throws IOException {
                String filePath = fileManagerConfig.getBaseFolder() + File.separator + user
                        + File.separator + service + File.separator + CONFIRMED_AOI_NAME;
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Download confirmed AOI to " + filePath);
                InputStream is = method.getResponseBodyAsStream();
                byte[] bytes = IOUtils.toByteArray(is);
                createCommittedFile(filePath, bytes);
            }

            /**
             * Download the acquisition list for the plan
             * 
             * @param method
             * @throws IOException
             */
            private void downloadConfirmedAcqPLan(HttpMethod method) throws IOException {
                String filePath = fileManagerConfig.getBaseFolder() + File.separator + user
                        + File.separator + service + File.separator + CONFIRMED_ACQ_PLAN;
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Generating acquisition list to " + filePath);
                byte[] buffer = new byte[1024];
                InputStream is = method.getResponseBodyAsStream();

                // Generate the zip
                FileOutputStream fos = new FileOutputStream(filePath);
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry(CONFIRMED_ACQ_PLAN_NAME);
                zos.putNextEntry(ze);

                int len;
                while ((len = is.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                is.close();
                zos.closeEntry();

                // remember close it
                zos.close();

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Acquisition list available in " + filePath);
            }

            /**
             * Create committed file with byte array with a byte array
             * 
             * @param filePath of the file
             * @param bytes to write
             * @return absolute path to the file
             * @throws IOException
             */
            private String createCommittedFile(String filePath, byte[] bytes) throws IOException {

                try {

                    // write bytes
                    File tmpFile = new File(filePath);

                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Appending bytes to " + tmpFile.getAbsolutePath());
                    }

                    // File channel to append bytes
                    @SuppressWarnings("resource")
                    FileChannel channel = new FileOutputStream(tmpFile, true).getChannel();
                    ByteBuffer buf = ByteBuffer.allocateDirect((int) bytes.length);

                    // put bytes
                    buf.put(bytes);

                    // Flips this buffer. The limit is set to the current position and then
                    // the position is set to zero. If the mark is defined then it is discarded.
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
                    HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                    String user, String password, ProxyInfo proxyInfo) {
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
     * @param action to perform
     * @param folder folder to browse
     * @param file to perform an operation
     * @param request servlet request
     * @param response servlet response
     * 
     * @return
     */
    @RequestMapping(value = "extJSbrowser", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    Object extJSbrowser(@RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "folder", required = false) String folder,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "oldName", required = false) String oldName,
            @RequestParam(value = "file", required = false) String file,
            HttpServletRequest request, HttpServletResponse response) {

        String finalFolder = folder != null && !folder.equals("root") ? folder : null;
        Integer level = folder != null ? folder.split(ControllerUtils.SEPARATOR).length - 1 : 0;
        
        if (EXTJS_FOLDER_NEW.equals(action)) {
            if (finalFolder != null && level == 2) {
                String serviceId = FilenameUtils.getName(finalFolder);
                String parent = FilenameUtils.getFullPathNoEndSeparator(finalFolder);
                String user = folder != null ? folder.split(ControllerUtils.SEPARATOR)[1] : null;
                if(serviceDAO.findByServiceId(serviceId) == null) {
                    serviceDAO.insert(new Service(serviceId, parent, user, "NEW"));
                }
            }
        }
        else if (EXTJS_FILE_DOWNLOAD.equals(action)) {
            if (finalFolder != null) {
                if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
                    finalFolder = finalFolder.replace(fileManagerConfig.getRootText(), "");
                }
            }
            String finalFile = file;
            if (finalFile != null) {
                if (finalFile.startsWith(fileManagerConfig.getRootText())) {
                    finalFile = finalFile.replace(fileManagerConfig.getRootText(), "");
                }
            }
            download(response, finalFile, getFilePath(finalFile, finalFolder));
            return null;
        }
        else if (EXTJS_SERVICES_LIST.equals(action)) {
            return getServicesList(folder);
        }
        else if (EXTJS_SENSORS_LIST.equals(action)) {
            return getSensorsList();
        }
        else if (EXTJS_SENSOR_MODES_LIST.equals(action)) {
            return getSensorModesList();
        }
        else if (EXTJS_SERVICE_SENSORS_LIST.equals(action)) {
            return getServiceSensorsList(folder, name);
        }
        
        //----
        return super.extJSbrowser(action, folder, name, oldName, file, request, response);
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
    @RequestMapping(value = "confirmServiceAOI", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    Object confirmService(@RequestParam(value = "user", required = true) String user,
            @RequestParam(value = "service", required = true) String service,
            HttpServletRequest request, HttpServletResponse httpServletResponse) {

        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (checkUserAndService(user, service, METHOD_CONFIRMED_AOI)) {
                downloadMethod = METHOD_CONFIRMED_AOI;
                proxyService.execute(request, new MockHttpServletResponse());
                response.put(ResponseConstants.SUCCESS, true);
                
                Service dbService = this.serviceDAO.findByServiceId(service);
                if (dbService != null) this.serviceDAO.updateServiceStatus(dbService, "AOI");
            } else {
                response.put(ResponseConstants.SUCCESS, false);
                response.put(ResponseConstants.ROOT, "Wrong user or service");
            }
        } catch (Exception e) {
            LOGGER.error("Error downloading finished AOI", e);
            response.put(ResponseConstants.SUCCESS, false);
        }
        return response;
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
    @RequestMapping(value = "confirmServiceAcqPlan", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody
    Object confirmServiceAcqPlan(@RequestParam(value = "user", required = true) String user,
            @RequestParam(value = "service", required = true) String service,
            HttpServletRequest request, HttpServletResponse httpServletResponse) {

        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (checkUserAndService(user, service, METHOD_CONFIRMED_ACQ_PLAN)) {
                downloadMethod = METHOD_CONFIRMED_ACQ_PLAN;
                proxyService.execute(request, new MockHttpServletResponse());

                Service dbService = this.serviceDAO.findByServiceId(service);
                if (dbService != null) this.serviceDAO.updateServiceStatus(dbService, "ACQUISITIONPLAN");
                response.put(ResponseConstants.SUCCESS, true);
            } else {
                response.put(ResponseConstants.SUCCESS, false);
                response.put(ResponseConstants.ROOT, "Wrong user or service");
            }
        } catch (Exception e) {
            LOGGER.error("Error on acquisition list generation", e);
            response.put(ResponseConstants.SUCCESS, false);
        }
        return response;
    }

    /**
     * Stores the sensors into the DB
     * 
     * @param user
     * @param service
     * @param request
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "putServiceSensorsList", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody
    Object putServiceSensorsList(@RequestParam(value = "user", required = true) String user,
            @RequestParam(value = "service", required = true) String service,
            HttpServletRequest request, HttpServletResponse httpServletResponse) {
        
        Map<String, Object> response = new HashMap<String, Object>();
        
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
          BufferedReader reader = request.getReader();
          while ((line = reader.readLine()) != null)
            jb.append(line);
        } catch (Exception e) {
            /*report an error*/ 
            LOGGER.error("Error reading JSON data from the request", e);
            response.put(ResponseConstants.SUCCESS, false);
        }

        try {
            List<Sensor> sensors = new ArrayList<Sensor>();
            
            JSONArray jsonArray = new JSONArray(jb.toString());
            
                
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject item = jsonArray.getJSONObject(i);
                sensors.add(new Sensor(item.getString("sensor_type"), new SensorMode(item.getString("sensor_mode"))));
            }
            
            this.serviceDAO.insertOrUpdate(service, sensors);
            
            response.put(ResponseConstants.SUCCESS, true);
        } catch (JSONException e) {
            // crash and burn
            LOGGER.error("Error parsing JSON request string", e);
            response.put(ResponseConstants.SUCCESS, false);
        }
        
        return response;
    }
    
    /**
     * Check if the user is the logged one and exists the service
     * 
     * @param user
     * @param service
     * @param method 
     * 
     * @return true if the logged user is the user and exists the service for the user
     */
    private boolean checkUserAndService(String user, String service, String method) {
        boolean checked = false;
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            String username = (String) sc.getAuthentication().getPrincipal();
            if (user != null && username != null && user.equals(username) && service != null) {
                checked = (new File(fileManagerConfig.getBaseFolder() + File.separator + user
                        + File.separator + service)).exists();
                
                if (METHOD_CONFIRMED_AOI.equals(method)) {
                    checked =  (new File(fileManagerConfig.getBaseFolder() + File.separator + user
                            + File.separator + service + File.separator + ACQ_LIST_FOLDER)).mkdirs();
                }
                else if (METHOD_CONFIRMED_ACQ_PLAN.equals(method)) {
                    checked =  (new File(fileManagerConfig.getBaseFolder() + File.separator + user
                            + File.separator + service + File.separator + PRODUCTS_FOLDER)).mkdirs();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Ungranted access", e);
        }
        return checked;
    }

    /**
     * Handler for upload files
     * 
     * @param operationId
     * @param gotHeaders
     * @param file uploaded
     * @param request
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public void upload(@RequestParam MultipartFile file,
            @RequestParam(required = false, defaultValue = "uploadedFile") String name,
            @RequestParam(required = false, defaultValue = "-1") int chunks,
            @RequestParam(required = false, defaultValue = "-1") int chunk,
            @RequestParam(required = false) String folder, HttpServletRequest request,
            HttpServletResponse servletResponse) throws IOException {
        String finalFolder = folder;
        if (finalFolder != null) {
            if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
                finalFolder = finalFolder.replace(fileManagerConfig.getRootText(), "");
            }
        }
        super.upload(file, name, chunks, chunk, finalFolder, request, servletResponse);
    }

    /**
     * Download a file
     * 
     * @param folder folder for the file
     * @param file to be downloaded
     * @param resp servlet response
     */
    @RequestMapping(value = "download", method = { RequestMethod.POST, RequestMethod.GET })
    public void downloadFile(@RequestParam(value = "folder", required = false) String folder,
            @RequestParam(value = "file", required = true) String file, HttpServletResponse resp) {
        String finalFolder = folder;
        if (finalFolder != null) {
            if (finalFolder.startsWith(fileManagerConfig.getRootText())) {
                finalFolder = finalFolder.replace(fileManagerConfig.getRootText(), "");
            }
        }
        String finalFile = file;
        if (finalFile != null) {
            if (finalFile.startsWith(fileManagerConfig.getRootText())) {
                finalFile = finalFile.replace(fileManagerConfig.getRootText(), "");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Download file "
                    + ((finalFolder != null) ? finalFolder + finalFile : finalFile));
        }
        super.downloadFile(finalFolder, finalFile, resp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.geosolutions.opensdi2.mvc.BaseFileManager#getFolderList(java.lang. String)
     */
    @Override
    protected List<Map<String, Object>> getFolderList(String folder) {
        List<Map<String, Object>> currentFolderList = super.getFolderList(folder);        
        
        FolderPermission permission = fileManagerConfig.getPermission(folder);

        // write operations available
        for (Map<String, Object> rootElement : currentFolderList) {
            rootElement.put("canRename", permission.canRename());
            rootElement.put("canDelete", permission.canDelete());
            rootElement.put("canCreateFolder", permission.canCreateFolder());
            rootElement.put("canUpload", permission.canUpload());
        }

        return currentFolderList;
    }
    
    /**
     * 
     * @param userid
     * @return
     */
    protected List<Map<String, Object>> getServicesList(String userid) {
        List<Service> services = this.serviceDAO.findByUser(userid);
        List<Map<String, Object>> currentFolderList = super.getFolderList(userid);        
        List<Map<String, Object>> servicesList = new LinkedList<Map<String, Object>>();        
        
        FolderPermission permission = fileManagerConfig.getPermission(userid);

        // write operations available
        for (Map<String, Object> rootElement : currentFolderList) {
            rootElement.put("canRename", permission.canRename());
            rootElement.put("canDelete", permission.canDelete());
            rootElement.put("canCreateFolder", permission.canCreateFolder());
            rootElement.put("canUpload", permission.canUpload());
            
            for (Service service : services) {
                if (rootElement.get("text").equals(service.getServiceId())) {
                    rootElement.put("isService", true);
                    rootElement.put("userId", service.getUser());
                    rootElement.put("status", service.getStatus());
                    rootElement.put("parent", service.getParent());
                    
                    if(service.getAoi() != null) {
                        AreaOfInterest aoi = service.getAoi();
                        rootElement.put("aoiStartTime", aoi.getStartTime());
                        rootElement.put("aoiEndTime", aoi.getEndTime());
                        rootElement.put("aoiGeometry", aoi.getTheGeom());
                        rootElement.put("aoiStatus", aoi.getStatus());
                        rootElement.put("aoiDescription", aoi.getDescription());
                    }
                    
                    servicesList.add(rootElement);
                }
            }
        }

        return servicesList;
    }

    /**
     * 
     * @return
     */
    private List<Map<String, Object>> getSensorModesList() {
        List<SensorMode> sensorModes = this.serviceDAO.getSensorModes();
        List<Map<String, Object>> modes = new ArrayList<Map<String,Object>>();
        
        for (SensorMode sensorMode : sensorModes) {
            Map<String, Object> rootElement = new HashMap<String, Object>();
            rootElement.put("text", sensorMode.getSensorMode());
            modes.add(rootElement);
        }
        
        return modes;
    }

    /**
     * 
     * @return
     */
    private List<Map<String, Object>> getSensorsList() {
        List<Sensor> sensors = this.serviceDAO.getSensors();
        List<Map<String, Object>> sensorTypes = new ArrayList<Map<String,Object>>();
        
        for (Sensor sensor : sensors) {
            Map<String, Object> rootElement = new HashMap<String, Object>();
            rootElement.put("text", sensor.getSensor());
            sensorTypes.add(rootElement);
        }
        
        return sensorTypes;
    }

    /**
     * 
     * @param userId
     * @param serviceId
     * @return
     */
    private List<Map<String, Object>> getServiceSensorsList(String userId, String serviceId) {
        List<Map<String, Object>> store = new ArrayList<Map<String,Object>>();
        List<Service> services = this.serviceDAO.findByUser(userId);
        
        if (services != null) {
            Service service = null;
            
            for (Service ss : services) {
                if (serviceId.equals(ss.getServiceId())) {
                    service = ss;
                }
            }
            
            if (service != null) {
                List<Sensor> sensors = service.getSensors();

                // add sensors
                for (Sensor sensor : sensors) {
                    Map<String, Object> rootElement = new HashMap<String, Object>();
                    rootElement.put("sensor_type", sensor.getSensor());
                    rootElement.put("sensor_mode", sensor.getSensorMode().getSensorMode());
                    store.add(rootElement);                    
                }
            }
        }
        
        return store;
    }

    /**
     * @return the proxyService
     */
    public ProxyService getProxyService() {
        return proxyService;
    }
}
