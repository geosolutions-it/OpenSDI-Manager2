package it.geosolutions.opensdi2.download;

import it.geosolutions.opensdi2.download.register.OrderInfo;
import it.geosolutions.opensdi2.download.register.OrderRegistrer;

/**
 * Base class for Download services that register downloads in a register.
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public abstract class BaseDownloadService implements DownloadService<Order>, OrderRegisterAware {
    /**
     * The register
     */
    private OrderRegistrer orderRegister;

    @Override
    public String registerOrder(Order o) {
        return this.getOrderRegister().registrer(o);
    }

    @Override
    public OrderRegistrer getOrderRegister() {
        return this.orderRegister;
    }

    @Override
    public void setOrderRegister(OrderRegistrer register) {
        this.orderRegister = register;
    }

    @Override
    public OrderInfo getOrderInfo(String id) {
        return getOrderRegister().getEntry(id);
    }

}
