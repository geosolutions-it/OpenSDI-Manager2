package it.geosolutions.opensdi2.download.order;

import it.geosolutions.opensdi2.download.Order;

import java.util.HashMap;
/**
 * Basic implementation of the order with a map
 * @author Lorenzo Natali, GeoSolutions
 *
 */
@SuppressWarnings("serial")
public class MapOrder extends HashMap<String,Object> implements Order  {

}
