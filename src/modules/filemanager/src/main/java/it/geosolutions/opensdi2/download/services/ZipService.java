package it.geosolutions.opensdi2.download.services;

import it.geosolutions.opensdi2.download.BaseDownloadService;
import it.geosolutions.opensdi2.download.Order;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Abastract implementation of a service that produces a ZIP from an order.
 * 
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public abstract class ZipService extends  BaseDownloadService {

	/**
	 * Compression Mehod (deflated by default( See <ZipOutputStream> for more information )
	 */
	private int compressionMethod = ZipOutputStream.DEFLATED;
	
	/**
	 * Compression Level (0 by default( See <ZipOutputStream> for more information )
	 */
	private int compressionLevel = 0;

	

	@Override
	public OutputStream getDownload(Order order, OutputStream out)
			throws IOException {
		final ZipOutputStream zipOut = new ZipOutputStream(out);
		zipOut.setLevel(compressionLevel);
		zipOut.setMethod(compressionMethod);
		// for each string of the order
		downloadOrder(order, zipOut);
		zipOut.flush();
		return zipOut;
	}

	
	/**
	 * Do the real download, creating the zip entries. 
	 * @param order the order to create the order
	 * @param zipOut the stream to write in
	 */
	protected abstract void downloadOrder(Order order, ZipOutputStream zipOut);

	/**
	 * Get the single file of the order
	 * 
	 * @param s
	 *            the order
	 * @return the list of file paths
	 */
	public abstract List<String> getFiles(String order);
	
	// SETTERS ABD GETTERS 
	/**
	 * Get the comression method 
	 * @return the method
	 */
	public int getCompressionMethod() {
		return compressionMethod;
	}

	/**
	 * Set the compression level
	 * @return the level
	 */
	public int getCompressionLevel() {
		return compressionLevel;
	}

	/**
	 * Set the compression Method See <ZipOutputStream>
	 * @param compressionMethod
	 */
	public void setCompressionMethod(int compressionMethod) {
		this.compressionMethod = compressionMethod;
	}
	
	/**
	 * Set the compression Level. See <ZipOutputStream>
	 * @param compressionLevel
	 */
	public void setCompressionLevel(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}

	
}
