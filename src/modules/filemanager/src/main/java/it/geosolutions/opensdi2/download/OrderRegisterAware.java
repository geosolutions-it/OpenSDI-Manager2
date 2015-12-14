package it.geosolutions.opensdi2.download;

import it.geosolutions.opensdi2.download.register.OrderRegistrer;

/**
 * Interface for a Order Register Aware class
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public interface OrderRegisterAware {
    /**
     * Set the register
     * 
     * @param register
     */
    void setOrderRegister(OrderRegistrer register);

    /**
     * get the register
     * 
     * @return the register
     */
    OrderRegistrer getOrderRegister();
}
