package it.geosolutions.opensdi2.download.register;

import it.geosolutions.opensdi2.download.Order;

import java.util.Date;


/**
 * Bean of a register entry
 * Contains the order, the date, the status
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class OrderRegisterEntry {
	
	/**
	 * Identifier
	 */
	private String id;
	/**
	 * The order
	 */
	private Order order;
	
	/**
	 * The registration date
	 */
	private Date registrationDate;
	
	/**
	 * The status
	 */
	private OrderStatus status;
	
	/**
	 * The progress (1 to 100)
	 */
	private short progress = 0;
	
	/**
	 * Create for the order
	 * @param order
	 */
	public OrderRegisterEntry(Order order){
		this.order = order;
		this.registrationDate = new Date();
		this.status = OrderStatus.WAIT;
		this.progress = 0;
	}
	
	/**
	 * Create for the order
	 * @param order
	 * @param status the status
	 */
	public OrderRegisterEntry(Order order, OrderStatus status){
		this(order);
		this.status =  status;
		this.progress = getProgress();
	}
	
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public short getProgress() {
		if(status == OrderStatus.READY){
			return 100;
		}if (status == OrderStatus.WAIT){
			return 0;
		}
		return progress;
	}

	public void setProgress(short progress) {
		this.progress = progress;
	}
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
