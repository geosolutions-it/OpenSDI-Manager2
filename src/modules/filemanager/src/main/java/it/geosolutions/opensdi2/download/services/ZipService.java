package it.geosolutions.opensdi2.download.services;

import it.geosolutions.opensdi2.download.DownloadService;
import it.geosolutions.opensdi2.download.Order;
import it.geosolutions.opensdi2.download.register.OrderRegistrer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Abastract implementation of a service that produces a ZIP from an order.
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public abstract class ZipService implements DownloadService<Order> {
	private static final Logger LOGGER = Logger.getLogger(ZipService.class);

	private OrderRegistrer orderRegister;

	@Override
	public OutputStream getDownload(Order order,
			OutputStream out) throws IOException {
		final ZipOutputStream zipOut = new ZipOutputStream(out);
		zipOut.setLevel(0);
		// for each string of the order
		downloadOrder(order, zipOut);
		zipOut.flush();
		return zipOut;
	}

	@Override
	public String registerOrder(Order o) {
		return this.getOrderRegister().registrer(o);
	}

	protected abstract void downloadOrder(Order order, ZipOutputStream zipOut);

	/**
	 * Get the single file of the order
	 * 
	 * @param s
	 *            the order
	 * @return the list of file paths
	 */
	public abstract List<String> getFiles(String order);

	@Override
	public OrderRegistrer getOrderRegister() {
		return this.orderRegister;
	}

	@Override
	public void setOrderRegister(OrderRegistrer register) {
		this.orderRegister = register;
	}

}
