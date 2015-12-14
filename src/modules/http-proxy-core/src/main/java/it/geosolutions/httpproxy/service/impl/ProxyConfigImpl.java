/*
 *  Copyright (C) 2007 - 2011 GeoSolutions S.A.S.
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
package it.geosolutions.httpproxy.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.ServletContextResource;

import it.geosolutions.httpproxy.service.ProxyConfig;
import it.geosolutions.httpproxy.utils.Utils;

/**
 * ProxyConfig class to define the proxy configuration.
 * 
 * @author Tobia Di Pisa at tobia.dipisa@geo-solutions.it
 * @author Alejandro Diaz
 */
public final class ProxyConfigImpl implements ProxyConfig {

    /* Writable properties to be parsed */
    private String hostNameList;

    private String mimeTypeWhiteList;

    private String methodsWhiteList;

    private String hostsWhiteList;

    private String reqtypeWhitelistCapabilities;

    private String reqtypeWhitelistGeostore;

    private String reqtypeWhitelistCsw;

    private String reqtypeWhitelistFeatureinfo;

    private String reqtypeWhitelistGeneric;

    private String defaultStreamByteSizeTmp;

    private String timeoutTmp;

    private String connection_timeout;

    private String max_total_connections;

    private String default_max_connections_per_host;

    private final static Logger LOGGER = Logger.getLogger(ProxyConfigImpl.class.toString());

    /**
     * A list of regular expressions describing hostnames the proxy is permitted to forward to
     */
    private Set<String> hostnameWhitelist = new HashSet<String>();

    /**
     * A list of regular expressions describing MIMETypes the proxy is permitted to forward
     */
    private Set<String> mimetypeWhitelist = new HashSet<String>();

    /**
     * A list of regular expressions describing Request Types the proxy is permitted to forward
     */
    private Set<String> reqtypeWhitelist = new HashSet<String>();

    /**
     * A list of regular expressions describing request METHODS the proxy is permitted to forward
     */
    private Set<String> methodsWhitelist = new HashSet<String>();

    /**
     * A list of regular expressions describing request HOSTS the proxy is permitted to forward
     */
    private Set<String> hostsWhitelist = new HashSet<String>();

    /**
     * The servlet context
     */
    private ServletContext context;

    /**
     * The path of the properties file
     */
    private String propertiesFilePath;

    /**
     * The request timeout
     */
    private int soTimeout = 30000;

    /**
     * The connection timeout
     */
    private int connectionTimeout = 30000;

    /**
     * The maximum total connections available
     */
    private int maxTotalConnections = 60;

    /**
     * The maximum connections available per host
     */
    private int defaultMaxConnectionsPerHost = 6;

    private int defaultStreamByteSize = 1024;

    /**
     * Locations proxy configuration list
     * 
     * @see http-proxy-default.xml
     */
    private Resource[] locations;

    /**
     * Default this bean name 'proxyConfig'.
     */
    private String beanName = "proxyConfig";

    /**
     * 
     */
    private Map<String, Long> timeModificationByLocation = new ConcurrentHashMap<String, Long>();

    /**
     * Default constructor
     */
    public ProxyConfigImpl() {
        super();
        configProxy();
    }

    /**
     * Provide the proxy configuration
     * 
     * @throws IOException
     */
    @Override
    public void configProxy() {

        try {
            // Load properties in getters
            getHostnameWhitelist();
            getMimetypeWhitelist();
            getMethodsWhitelist();
            getHostnameWhitelist();
            getReqtypeWhitelist();

            // /////////////////////////////////////////////////
            // Load byte size configuration
            // /////////////////////////////////////////////////

            String bytesSize = defaultStreamByteSizeTmp;
            this.setDefaultStreamByteSize(
                    bytesSize != null ? Integer.parseInt(bytesSize) : this.defaultStreamByteSize);

            // /////////////////////////////////////////////////
            // Load connection manager configuration
            // /////////////////////////////////////////////////

            String timeout = timeoutTmp;
            this.setSoTimeout(timeout != null ? Integer.parseInt(timeout) : this.soTimeout);

            String conn_timeout = connection_timeout;
            this.setConnectionTimeout(
                    conn_timeout != null ? Integer.parseInt(conn_timeout) : this.connectionTimeout);

            String max_conn = max_total_connections;
            this.setMaxTotalConnections(
                    max_conn != null ? Integer.parseInt(max_conn) : this.maxTotalConnections);

            String def_conn_host = default_max_connections_per_host;
            this.setMaxTotalConnections(def_conn_host != null ? Integer.parseInt(def_conn_host)
                    : this.defaultMaxConnectionsPerHost);

        } catch (NumberFormatException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, "Error parsing the proxy properties file using default",
                        e);

            this.setSoTimeout(this.soTimeout);
            this.setConnectionTimeout(this.connectionTimeout);
            this.setMaxTotalConnections(this.maxTotalConnections);
            this.setMaxTotalConnections(this.defaultMaxConnectionsPerHost);
            this.setDefaultStreamByteSize(this.defaultStreamByteSize);
        }
    }

    /**
     * Reload proxy configuration reading {@link ProxyConfigImpl#locations}
     */
    public void reloadProxyConfig() {
        if (locations != null) {
            for (Resource location : locations) {
                try {
                    if (location.exists()) {
                        trackLocation(location);
                    } else {
                        // Try to load from file system:
                        String path = null;
                        if (location instanceof ClassPathResource) {
                            // This instance is running without web context
                            path = ((ClassPathResource) location).getPath();
                        } else if (location instanceof ServletContextResource) {
                            // This instance is running in a web context
                            path = ((ServletContextResource) location).getPath();
                        }
                        if (path != null) {
                            Resource alternative = new UrlResource("file:/" + path);
                            if (alternative.exists()) {
                                trackLocation(alternative);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error overriding the proxy configuration ", e);
                }
            }
        } else {
            LOGGER.log(Level.SEVERE, "Can't observe locations for proxy configuration");
        }
    }

    /**
     * Save last modification of the file to track it and override this bean properties if the file has been changed
     * 
     * @param location Resource location of the file
     * 
     * @throws IOException
     */
    private void trackLocation(Resource location) throws IOException {
        String fileName = location.getFilename();
        Long lastModified = location.lastModified();
        if (!(timeModificationByLocation.containsKey(fileName))) {
            // Save last modification timestamp
            timeModificationByLocation.put(fileName, lastModified);
        } else if (isModified(fileName, lastModified)) {
            // Override proxy configuration
            LOGGER.log(Level.INFO,
                    "Proxy configuration has changed at runtime in file '" + fileName + "'");
            overrideProperties(location);
        }
    }

    /**
     * Determines if the file has been modified since the last check.
     */
    public boolean isModified(String fileName, long lastModified) {
        long interval = lastModified - timeModificationByLocation.get(fileName);
        timeModificationByLocation.put(fileName, lastModified);
        return Math.abs(interval) > 0;
    }

    /**
     * Override this fields with a location properties
     * 
     * @param location
     * 
     * @throws IOException if read properties throw an error
     */
    private void overrideProperties(Resource location) throws IOException {
        Properties props = new Properties();
        props.load(location.getInputStream());
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            try {
                String key = (String) keys.nextElement();
                if (key.startsWith(beanName + ".")) {
                    String parameter = key.replace(beanName + ".", "");
                    Field field = ReflectionUtils.findField(this.getClass(), parameter);
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, this, props.getProperty(key));
                    LOGGER.log(Level.INFO, "[override]: " + key + "=" + props.getProperty(key));
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error overriding the proxy configuration ", e);
            }
        }
    }

    /**
     * @return the soTimeout
     */
    @Override
    public int getSoTimeout() {
        return soTimeout;
    }

    /**
     * @param soTimeout the soTimeout to set
     */
    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    /**
     * @return the connectionTimeout
     */
    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout the connectionTimeout to set
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @return the maxTotalConnections
     */
    @Override
    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    /**
     * @param maxTotalConnections the maxTotalConnections to set
     */
    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    /**
     * @return the defaultMaxConnectionsPerHost
     */
    @Override
    public int getDefaultMaxConnectionsPerHost() {
        return defaultMaxConnectionsPerHost;
    }

    /**
     * @param defaultMaxConnectionsPerHost the defaultMaxConnectionsPerHost to set
     */
    public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
        this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
    }

    /**
     * @return the hostnameWhitelist
     */
    @Override
    public Set<String> getHostnameWhitelist() {

        // ////////////////////////////////////////////////////////////
        // Load proxy configuration white lists from properties file
        // ////////////////////////////////////////////////////////////

        Set<String> p = Utils.parseWhiteList(hostNameList);
        if (p != null)
            this.hostnameWhitelist = p;

        return hostnameWhitelist;
    }

    /**
     * @param hostnameWhitelist the hostnameWhitelist to set
     */
    public void setHostnameWhitelist(Set<String> hostnameWhitelist) {
        this.hostnameWhitelist = hostnameWhitelist;
    }

    /**
     * @return the mimetypeWhitelist
     */
    @Override
    public Set<String> getMimetypeWhitelist() {

        Set<String> p = Utils.parseWhiteList(mimeTypeWhiteList);
        if (p != null)
            this.setHostnameWhitelist(p);

        return mimetypeWhitelist;
    }

    /**
     * @param mimetypeWhitelist the mimetypeWhitelist to set
     */
    public void setMimetypeWhitelist(Set<String> mimetypeWhitelist) {
        this.mimetypeWhitelist = mimetypeWhitelist;
    }

    /**
     * @return the reqtypeWhitelist
     */
    @Override
    public Set<String> getReqtypeWhitelist() {

        // ////////////////////////////////////////
        // Read various request type properties
        // ////////////////////////////////////////

        Set<String> rt = new HashSet<String>();
        String s = reqtypeWhitelistCapabilities;
        if (s != null)
            rt.add(s);

        s = reqtypeWhitelistGeostore;
        if (s != null)
            rt.add(s);

        s = reqtypeWhitelistCsw;
        if (s != null)
            rt.add(s);

        s = reqtypeWhitelistFeatureinfo;
        if (s != null)
            rt.add(s);

        s = reqtypeWhitelistGeneric;
        if (s != null)
            rt.add(s);

        this.reqtypeWhitelist = rt;

        return reqtypeWhitelist;
    }

    /**
     * @param reqtypeWhitelist the reqtypeWhitelist to set
     */
    public void setReqtypeWhitelist(Set<String> reqtypeWhitelist) {
        this.reqtypeWhitelist = reqtypeWhitelist;
    }

    /**
     * @return the methodsWhitelist
     */
    @Override
    public Set<String> getMethodsWhitelist() {

        Set<String> p = Utils.parseWhiteList(methodsWhiteList);
        if (p != null)
            this.methodsWhitelist = p;

        return methodsWhitelist;
    }

    /**
     * @param methodsWhitelist the methodsWhitelist to set
     */
    public void setMethodsWhitelist(Set<String> methodsWhitelist) {
        this.methodsWhitelist = methodsWhitelist;
    }

    /**
     * @return the hostsWhitelist
     */
    @Override
    public Set<String> getHostsWhitelist() {

        Set<String> p = Utils.parseWhiteList(hostsWhiteList);
        if (p != null)
            this.hostsWhitelist = p;

        return hostsWhitelist;
    }

    /**
     * @param hostsWhitelist the hostsWhitelist to set
     */
    public void setHostsWhitelist(Set<String> hostsWhitelist) {
        this.hostsWhitelist = hostsWhitelist;
    }

    /**
     * @return the context
     */
    public ServletContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ServletContext context) {
        this.context = context;
    }

    /**
     * @return the propertiesFilePath
     */
    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    /**
     * @param propertiesFilePath the propertiesFilePath to set
     */
    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    /**
     * @return the defaultStreamByteSize
     */
    @Override
    public int getDefaultStreamByteSize() {
        return defaultStreamByteSize;
    }

    /**
     * @param defaultStreamByteSize the defaultStreamByteSize to set
     */
    public void setDefaultStreamByteSize(int defaultStreamByteSize) {
        this.defaultStreamByteSize = defaultStreamByteSize;
    }

    /**
     * @return the hostNameList
     */
    public String getHostNameList() {
        return hostNameList;
    }

    /**
     * @param hostNameList the hostNameList to set
     */
    public void setHostNameList(String hostNameList) {
        this.hostNameList = hostNameList;
    }

    /**
     * @return the mimeTypeWhiteList
     */
    public String getMimeTypeWhiteList() {
        return mimeTypeWhiteList;
    }

    /**
     * @param mimeTypeWhiteList the mimeTypeWhiteList to set
     */
    public void setMimeTypeWhiteList(String mimeTypeWhiteList) {
        this.mimeTypeWhiteList = mimeTypeWhiteList;
    }

    /**
     * @return the methodsWhiteList
     */
    public String getMethodsWhiteList() {
        return methodsWhiteList;
    }

    /**
     * @param methodsWhiteList the methodsWhiteList to set
     */
    public void setMethodsWhiteList(String methodsWhiteList) {
        this.methodsWhiteList = methodsWhiteList;
    }

    /**
     * @return the hostsWhiteList
     */
    public String getHostsWhiteList() {
        return hostsWhiteList;
    }

    /**
     * @param hostsWhiteList the hostsWhiteList to set
     */
    public void setHostsWhiteList(String hostsWhiteList) {
        this.hostsWhiteList = hostsWhiteList;
    }

    /**
     * @return the reqtypeWhitelistCapabilities
     */
    public String getReqtypeWhitelistCapabilities() {
        return reqtypeWhitelistCapabilities;
    }

    /**
     * @param reqtypeWhitelistCapabilities the reqtypeWhitelistCapabilities to set
     */
    public void setReqtypeWhitelistCapabilities(String reqtypeWhitelistCapabilities) {
        this.reqtypeWhitelistCapabilities = reqtypeWhitelistCapabilities;
    }

    /**
     * @return the reqtypeWhitelistGeostore
     */
    public String getReqtypeWhitelistGeostore() {
        return reqtypeWhitelistGeostore;
    }

    /**
     * @param reqtypeWhitelistGeostore the reqtypeWhitelistGeostore to set
     */
    public void setReqtypeWhitelistGeostore(String reqtypeWhitelistGeostore) {
        this.reqtypeWhitelistGeostore = reqtypeWhitelistGeostore;
    }

    /**
     * @return the reqtypeWhitelistCsw
     */
    public String getReqtypeWhitelistCsw() {
        return reqtypeWhitelistCsw;
    }

    /**
     * @param reqtypeWhitelistCsw the reqtypeWhitelistCsw to set
     */
    public void setReqtypeWhitelistCsw(String reqtypeWhitelistCsw) {
        this.reqtypeWhitelistCsw = reqtypeWhitelistCsw;
    }

    /**
     * @return the reqtypeWhitelistFeatureinfo
     */
    public String getReqtypeWhitelistFeatureinfo() {
        return reqtypeWhitelistFeatureinfo;
    }

    /**
     * @param reqtypeWhitelistFeatureinfo the reqtypeWhitelistFeatureinfo to set
     */
    public void setReqtypeWhitelistFeatureinfo(String reqtypeWhitelistFeatureinfo) {
        this.reqtypeWhitelistFeatureinfo = reqtypeWhitelistFeatureinfo;
    }

    /**
     * @return the reqtypeWhitelistGeneric
     */
    public String getReqtypeWhitelistGeneric() {
        return reqtypeWhitelistGeneric;
    }

    /**
     * @param reqtypeWhitelistGeneric the reqtypeWhitelistGeneric to set
     */
    public void setReqtypeWhitelistGeneric(String reqtypeWhitelistGeneric) {
        this.reqtypeWhitelistGeneric = reqtypeWhitelistGeneric;
    }

    /**
     * @return the defaultStreamByteSizeTmp
     */
    public String getDefaultStreamByteSizeTmp() {
        return defaultStreamByteSizeTmp;
    }

    /**
     * @param defaultStreamByteSizeTmp the defaultStreamByteSizeTmp to set
     */
    public void setDefaultStreamByteSizeTmp(String defaultStreamByteSizeTmp) {
        this.defaultStreamByteSizeTmp = defaultStreamByteSizeTmp;
    }

    /**
     * @return the timeoutTmp
     */
    public String getTimeoutTmp() {
        return timeoutTmp;
    }

    /**
     * @param timeoutTmp the timeoutTmp to set
     */
    public void setTimeoutTmp(String timeoutTmp) {
        this.timeoutTmp = timeoutTmp;
    }

    /**
     * @return the connection_timeout
     */
    public String getConnection_timeout() {
        return connection_timeout;
    }

    /**
     * @param connection_timeout the connection_timeout to set
     */
    public void setConnection_timeout(String connection_timeout) {
        this.connection_timeout = connection_timeout;
    }

    /**
     * @return the max_total_connections
     */
    public String getMax_total_connections() {
        return max_total_connections;
    }

    /**
     * @param max_total_connections the max_total_connections to set
     */
    public void setMax_total_connections(String max_total_connections) {
        this.max_total_connections = max_total_connections;
    }

    /**
     * @return the default_max_connections_per_host
     */
    public String getDefault_max_connections_per_host() {
        return default_max_connections_per_host;
    }

    /**
     * @param default_max_connections_per_host the default_max_connections_per_host to set
     */
    public void setDefault_max_connections_per_host(String default_max_connections_per_host) {
        this.default_max_connections_per_host = default_max_connections_per_host;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * @return location array
     */
    public Resource[] getLocations() {
        return locations;
    }

    /**
     * Set default locations for proxy config
     * 
     * @param locations array
     */
    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }

}
