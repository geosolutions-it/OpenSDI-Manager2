package it.geosolutions.mariss.download;

import java.util.ArrayList;
import java.util.HashMap;

public class DownloadTestUtils {
    public static void addToOrder(final String serviceId, final String productId,
            final String fileName1, MarissOrder order) {
        HashMap<String, ArrayList<String>> serviceObj = new HashMap<String, ArrayList<String>>();
        ArrayList<String> productList = new ArrayList<String>();
        productList.add(fileName1);
        serviceObj.put(productId, productList);
        order.put(serviceId, serviceObj);
    }
}
