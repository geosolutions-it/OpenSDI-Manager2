/**
 * 
 */
package it.geosolutions.opensdi2.rest.plugin;

import it.geosolutions.opensdi2.rest.RestItemParameter;

/**
 * @author alessio.fabiani
 *
 */
public class RestGeoBatchParameter implements RestItemParameter {

	private String paramName;
	private Object paramValue;

	/**
	 * @param paramName
	 * @param paramValue
	 */
	public RestGeoBatchParameter(String paramName, Object paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestItemParameter#getParamName()
	 */
	@Override
	public String getParamName() {
		return paramName;
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestItemParameter#getParamValue()
	 */
	@Override
	public Object getParamValue() {
		return paramValue;
	}

}
