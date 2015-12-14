package it.geosolutions.opensdi2.download.order;

import java.util.List;

import it.geosolutions.opensdi2.download.Order;

/**
 * Basic implementation of the order with List of Strings
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class ListOrder implements Order {
    protected List<String> order;

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }
}
