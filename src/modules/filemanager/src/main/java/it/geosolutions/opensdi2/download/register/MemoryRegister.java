package it.geosolutions.opensdi2.download.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import it.geosolutions.opensdi2.download.Order;

/**
 * Simple implementation implementation of <OrderRegistrer>
 * 
 * @author Lorenzo Natali,GeoSolutions
 *
 */
public class MemoryRegister implements OrderRegistrer {

    Map<String, OrderInfo> orders = new HashMap<String, OrderInfo>();

    @Override
    public String registrer(Order order) {
        OrderInfo entry = new OrderInfo(order);
        return register(entry);
    }

    @Override
    public OrderInfo getEntry(String id) {
        return orders.get(id);
    }

    @Override
    public OrderInfo removeEntry(String id) {
        return orders.remove(id);
    }

    @Override
    public String registrer(Order order, OrderStatus status) {

        OrderInfo entry = new OrderInfo(order, status);
        return register(entry);
    }

    private String register(OrderInfo entry) {
        String uniqueID = UUID.randomUUID().toString();
        entry.setId(uniqueID);
        orders.put(uniqueID, entry);
        return uniqueID;
    }

    @Override
    public void updateEntry(String id, OrderInfo entry) {
        orders.put(id, entry);

    }

    @Override
    public List<OrderInfo> getOrders() {
        Set<String> keys = orders.keySet();
        List<OrderInfo> ret = new ArrayList<OrderInfo>();
        for (String s : keys) {
            ret.add(orders.get(s));
        }
        return ret;
    }

}
