package it.geosolutions.opensdi2.download.register;

/**
 * Status enumeration fo the Order
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public enum OrderStatus {
	/**
	 * The order is registered but not started
	 */
	WAIT,
	/**
	 * The order is in process
	 */
	STARTED,
	/**
	 * The order is ready, finished
	 */
	READY
}
