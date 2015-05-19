package it.geosolutions.opensdi2.download.register;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Simple Evictor for download orders 
 * @author Lorenzo Natali
 *
 */
public class RegisterEvictor {
	private static final Logger LOGGER = Logger.getLogger(RegisterEvictor.class);
	private OrderRegistrer register;
	private int maxSeconds;
	public void purge(){
		LOGGER.info("** order eviction started **");
		List<OrderRegisterEntry> entries = register.getOrders();
		long purgeTime = System.currentTimeMillis() - (this.maxSeconds * 1000);
		for(OrderRegisterEntry entry : entries){
			if(entry.getRegistrationDate().getTime() < purgeTime){
				LOGGER.info("remove entry" + entry.getId());
				register.removeEntry(entry.getId());
			}
		}
		LOGGER.info("** order eviction ended **");
	};
	
	//GETTERS AND SETTERS
	public OrderRegistrer getRegister() {
		return register;
	}
	public void setRegister(OrderRegistrer register) {
		this.register = register;
	}

	public int getMaxSeconds() {
		return maxSeconds;
	}

	public void setMaxSeconds(int maxSeconds) {
		this.maxSeconds = maxSeconds;
	}
}
