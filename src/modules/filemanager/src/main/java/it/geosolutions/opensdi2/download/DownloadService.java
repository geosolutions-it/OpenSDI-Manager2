package it.geosolutions.opensdi2.download;

import it.geosolutions.opensdi2.download.register.OrderRegistrer;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Service to get custom zip output stream for a list of files provided
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public interface DownloadService<ORDER extends Order> {
	/**
	 * Register a new order to the download service
	 * @param o
	 * @return
	 */
	public String registerOrder(ORDER o);
	
	/**
	 * Get the download for the order provided
	 * @param order
	 * @param out outputstram to write to 
	 * @return
	 * @throws IOException
	 */
	public OutputStream getDownload(ORDER order, OutputStream out) throws IOException;
	
	/**
	 * Set the register
	 * @param register
	 */
	void setOrderRegister(OrderRegistrer register);
	
	/**
	 * get the register
	 * @return the register
	 */
	OrderRegistrer getOrderRegister();
}
