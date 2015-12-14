package it.geosolutions.mariss.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import it.geosolutions.opensdi2.download.Order;
import it.geosolutions.opensdi2.download.register.OrderStatus;
import it.geosolutions.opensdi2.download.services.ZipService;

/**
 * NPA Portal Implementation of download service.
 * 
 * @author Lorenzo Natali
 *
 */
public class MarissDownloadService extends ZipService {

    private static final Logger LOGGER = Logger.getLogger(ZipService.class);

    @Override
    public String registerOrder(Order order) {
        // all orders are ready from the beginning
        return getOrderRegister().registrer(order, OrderStatus.READY);
    }

    @Override
    public List<String> getFiles(String order) {

        List<String> files = new ArrayList<String>();
        // TODO create the list of files
        return files;
    }

    @Override
    protected void downloadOrder(Order order, ZipOutputStream zipOut) {
        try {
            if (order instanceof MarissOrder) {
                downloadOrder((MarissOrder) order, zipOut);
            } else {
                String message = "Unable to manage the order" + order;
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
            }
        } catch (Exception e) {
            String message = "Error downloading the download for order" + order;
            LOGGER.error(message, e);
            throw new RejectedExecutionException(
                    "Error downloading the download for order" + order);
        }

    }

    /**
     * Creates the real download
     * 
     * @param order
     * @param zipOut
     * @throws IOException
     */
    private void downloadOrder(MarissOrder order, final ZipOutputStream zipOut) throws IOException {
        Map<String, String> orderList = order.getFilesToCreate();
        LOGGER.info("generating zip file for order:" + order);
        LOGGER.debug("******* File List *******");
        try {

            for (String sfile : orderList.keySet()) {
                addFileToZip(zipOut, sfile, orderList.get(sfile));
            }
        } catch (IOException e1) {
            LOGGER.error("Error during download creation", e1);
            throw e1;
        } catch (Exception e) {
            LOGGER.error("Error during download creation", e);
        } finally {
            zipOut.close();
        }
    }

    /**
     * Add a file to the zip
     * 
     * @param zipOut the stream to write
     * @param file the file to create in the zip
     * @param fullPath the path of the file to write into the zip, renamed as "file"
     * @throws IOException
     */
    private void addFileToZip(final ZipOutputStream zipOut, String file, String fullPath)
            throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(fullPath));

            LOGGER.debug("file:" + file);
            ZipEntry e = new ZipEntry(file);
            zipOut.putNextEntry(e);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
        } finally {
            fis.close();
        }

    }

}
