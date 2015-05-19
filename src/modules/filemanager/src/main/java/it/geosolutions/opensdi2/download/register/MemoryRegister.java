package it.geosolutions.opensdi2.download.register;

import it.geosolutions.opensdi2.download.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Simple implementation implementation of <OrderRegistrer>
 * @author Lorenzo Natali,GeoSolutions
 *
 */
public class MemoryRegister implements OrderRegistrer {

	Map<String,OrderRegisterEntry> orders = new HashMap<String,OrderRegisterEntry>();
	@Override
	public String registrer(Order order) {
		OrderRegisterEntry entry = new OrderRegisterEntry(order);
		return register(entry);
	}

	@Override
	public OrderRegisterEntry getEntry(String id) {
		return orders.get(id);
	}

	@Override
	public OrderRegisterEntry removeEntry(String id) {
		return orders.remove(id);
	}

	@Override
	public String registrer(Order order, OrderStatus status) {
		
		OrderRegisterEntry entry = new OrderRegisterEntry(order, status);
		return register(entry);
	}

	private String register(OrderRegisterEntry entry) {
		String uniqueID = UUID.randomUUID().toString();
		entry.setId(uniqueID);
		orders.put(uniqueID,entry);
		return uniqueID;
	}

	@Override
	public void updateEntry(String id, OrderRegisterEntry entry) {
		orders.put(id, entry);
		
	}

	@Override
	public List<OrderRegisterEntry> getOrders() {
		Set<String> keys = orders.keySet();
		List<OrderRegisterEntry> ret = new ArrayList<OrderRegisterEntry>();
		for(String s : keys){
			ret.add(orders.get(s));
		}
		return ret;
	}

}
