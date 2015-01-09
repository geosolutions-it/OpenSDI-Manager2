package it.geosolutions.opensdi2.model.mapping.facade;

import java.util.Set;

import it.geosolutions.httpproxy.service.ProxyConfig;

/**
 * A bean to wrap ProxyConfig
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class ProxyConfigBean implements ProxyConfig{
	
	private int soTimeout;
	private int connectionTimeout;
	private int maxTotalConnection;
	private int defaultMaxConnectionsPerHost;
	private Set<String> hostnameWhitelist;
	private Set<String> mimetypeWhitelist;
	private Set<String> reqtypeWhitelist;
	private Set<String> methodsWhitelist;
	private Set<String> hostsWhitelist;
	private int defaultStreamByteSize;
	
	public ProxyConfigBean(ProxyConfig proxyConfig) {
		soTimeout = proxyConfig.getSoTimeout();
		connectionTimeout = proxyConfig.getConnectionTimeout();
		maxTotalConnection = proxyConfig.getMaxTotalConnections();
		defaultMaxConnectionsPerHost = proxyConfig.getDefaultMaxConnectionsPerHost();
		hostnameWhitelist = proxyConfig.getHostnameWhitelist();
		mimetypeWhitelist = proxyConfig.getMimetypeWhitelist();
		reqtypeWhitelist = proxyConfig.getReqtypeWhitelist();
		methodsWhitelist = proxyConfig.getMethodsWhitelist();
		hostsWhitelist = proxyConfig.getHostsWhitelist();
		defaultStreamByteSize = proxyConfig.getDefaultStreamByteSize();
		
	}

	public int getMaxTotalConnection() {
		return maxTotalConnection;
	}

	public void setMaxTotalConnection(int maxTotalConnection) {
		this.maxTotalConnection = maxTotalConnection;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
		this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
	}

	public void setHostnameWhitelist(Set<String> hostnameWhitelist) {
		this.hostnameWhitelist = hostnameWhitelist;
	}

	public void setMimetypeWhitelist(Set<String> mimetypeWhitelist) {
		this.mimetypeWhitelist = mimetypeWhitelist;
	}

	public void setReqtypeWhitelist(Set<String> reqtypeWhitelist) {
		this.reqtypeWhitelist = reqtypeWhitelist;
	}

	public void setMethodsWhitelist(Set<String> methodsWhitelist) {
		this.methodsWhitelist = methodsWhitelist;
	}

	public void setHostsWhitelist(Set<String> hostsWhitelist) {
		this.hostsWhitelist = hostsWhitelist;
	}

	public void setDefaultStreamByteSize(int defaultStreamByteSize) {
		this.defaultStreamByteSize = defaultStreamByteSize;
	}

	@Override
	public void configProxy() {
		
	}

	@Override
	public int getSoTimeout() {

		return soTimeout;
	}

	@Override
	public int getConnectionTimeout() {

		return connectionTimeout;
	}

	@Override
	public int getMaxTotalConnections() {
		return maxTotalConnection;
	}

	@Override
	public int getDefaultMaxConnectionsPerHost() {
		return defaultMaxConnectionsPerHost;
	}

	@Override
	public Set<String> getHostnameWhitelist() {
		return hostnameWhitelist;
	}

	@Override
	public Set<String> getMimetypeWhitelist() {

		return mimetypeWhitelist;
	}

	@Override
	public Set<String> getReqtypeWhitelist() {
		return reqtypeWhitelist;
	}

	@Override
	public Set<String> getMethodsWhitelist() {
		return methodsWhitelist;
	}

	@Override
	public Set<String> getHostsWhitelist() {
		return hostsWhitelist;
	}

	@Override
	public int getDefaultStreamByteSize() {
		return defaultStreamByteSize;
	}

}
