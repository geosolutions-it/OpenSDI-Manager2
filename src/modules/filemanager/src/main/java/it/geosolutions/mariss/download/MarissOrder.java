package it.geosolutions.mariss.download;

import it.geosolutions.opensdi2.download.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class MarissOrder extends
		HashMap<String, HashMap<String, ArrayList<String>>> implements Order {
	/**
	 * Method that provides the files to create in from this order
	 * 
	 * @return a map with the path of the file to create and the path of the
	 *         file
	 */
	public Map<String, String> getFilesToCreate() {
		Map<String, String> filesToCreate = new HashMap<String, String>();
		for (String serviceId : keySet()) {
			HashMap<String, ArrayList<String>> serviceObj = get(serviceId);
			if (serviceObj != null) {
				for (String productId : serviceObj.keySet()) {
					ArrayList<String> productList = serviceObj.get(productId);
					// create a file with a path serviceID/productId/files
					// as the key of the map and the path to the file as
					// the value of the map
					for (String productFilePath : productList) {

						String objPath = serviceId + "/" + productId  + "/"
								+ getFileName(productFilePath);
						filesToCreate.put(objPath,productFilePath);
					}
				}
			}

		}
		return filesToCreate;
	}

	private String getFileName(String productFilePath) {
		String fname = FilenameUtils.getName(productFilePath);
		int i = fname.lastIndexOf("_s_");
		if( i>0 && i< fname.length() - 4){
			return fname.substring(i + 3);
		}
		return fname;
	}
}
