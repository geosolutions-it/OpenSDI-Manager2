package it.geosolutions.opensdi2.download.register;

import java.util.List;

import it.geosolutions.opensdi2.download.Order;

/**
 * Interface for an Order Registry
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public interface OrderRegistrer {
    /**
     * Register an order into the Register
     * 
     * @param order the order to register
     * @return the id of the order
     */
    public String registrer(Order order);

    /**
     * Register an order into the Register with a predefined status
     * 
     * @param order
     * @param status
     * @return the id of the order
     */
    public String registrer(Order order, OrderStatus status);

    /**
     * Get an order from the registry, by Id
     * 
     * @param id the Id
     * @return the entry of the registry
     */
    public OrderInfo getEntry(String id);

    /**
     * Delete an entry from the register
     * 
     * @param id the Identifier of the Entry
     * @return the entry removed
     */
    public OrderInfo removeEntry(String id);

    /**
     * Updates an entry
     * 
     * @param id the id in the registry
     * @param entry
     */
    public void updateEntry(String id, OrderInfo entry);

    /**
     * Get all current orders
     * 
     * @return
     */
    public List<OrderInfo> getOrders();
}
