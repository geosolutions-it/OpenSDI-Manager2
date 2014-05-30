package it.geosolutions.servicebox.utils;

import it.geosolutions.servicebox.FileUploadCallback;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * Utilities Class to support XML/JSON translation 
 * for GeoExplorer Configuration
 * @author Lorenzo Natali
 * 
 *
 */
public final class Utilities {

	private Utilities() {
		
	}

	/**
	 * This function converts String XML to Document object
	 * @param in - XML String
	 * @return Document object
	 */
	public static Document parseXmlFile(String in) {
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(in));
	        return db.parse(is);
	    } catch (ParserConfigurationException e) {
	        throw new RuntimeException(e);
	    } catch (SAXException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}
	
	/**
     * This function moves a Node (by tag name) 
     * as First Child of another Node (by tag name too)
     * Needed to put sources on top in FDH addLayers configuration.
     * @param d - Document object
     * @param destTagName Tag name of Destination Node
     * @param firstTagName Tag name of the Node that will be put at First.
     * 
     */
	public static void putAtFirst(Document d,String destTagName,String firstTagName) throws DOMException  {
    	
    	Node s= d.getElementsByTagName(destTagName).item(0);
		Node firstEl= s.getFirstChild();		
		Node fdh=d.getElementsByTagName(firstTagName).item(0);
		s.removeChild(fdh);
		s.insertBefore(fdh, firstEl);
	    
    }

	/**
	 * Write a simple message on response
	 * 
	 * @param response
	 * @param text
	 * @throws IOException
	 */
	public static void writeResponse(HttpServletResponse response, String text, Logger LOGGER)
			throws IOException {
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(text);
		} catch (IOException e) {
			if (LOGGER != null && LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (Exception e) {
				if (LOGGER != null && LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE, "Error closing response stream ",
							e);
			}
		}
	}

	/**
	 * Write a JSON error message on response
	 * 
	 * @param response
	 * @param errorCode
	 * @param errorDetails
	 * @param errorMessage
	 * @param LOGGER
	 * @throws IOException
	 */
	public static void writeError(HttpServletResponse response, int errorCode, Map<String, Object> errorDetails, String errorMessage, Logger LOGGER)
			throws IOException {
		Map<String, Object> messageJSON = new HashMap<String, Object>();
		messageJSON.put(JSON_MODEL.SUCCESS, false);
		messageJSON.put(JSON_MODEL.ERROR_CODE, errorCode);
		if(errorDetails != null){
			messageJSON.put(JSON_MODEL.DETAILS, errorDetails);
		}
		messageJSON.put(JSON_MODEL.ERROR_MESSAGE, errorMessage);
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			response.setContentType("text/html");
			writer.write(mapper.writeValueAsString(messageJSON));
		} catch (IOException e) {
			if (LOGGER != null && LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (Exception e) {
				if (LOGGER != null && LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE, "Error closing response stream ",
							e);
			}
		}
	}
	
	/**
	 * Model to write JSON error information on the response
	 * <br/><br/>
     * The format for an error response is:
	 * <br/>
	 * <pre>{
     *      "errorMessage":"message",
     *      "details":{`details map`},
     *      "errorCode":`code of the error`,
     *      "success":false
     * }</pre>
	 * 
	 * @author adiaz
	 *
	 */
	public static class JSON_MODEL{
		
		/**
		 * Success key
		 */
		public static final String SUCCESS = "success";
		
		/**
		 * Error code key
		 */
		public static final String ERROR_CODE = "errorCode";
		
		/**
		 * Error details key
		 */
		public static final String DETAILS = "details";
		
		/**
		 * Error message key
		 */
		public static final String ERROR_MESSAGE = "errorMessage";
		
		/**
		 * Known error codes for the JSON model
		 * 
		 * @author adiaz
		 *
		 */
		public enum KNOWN_ERRORS{
			/**
			 * Maximum items exceeded on upload action
			 * 
			 * @see FileUploadCallback
			 */
			MAX_ITEMS,
			/**
			 * Maximum item size exceeded on upload action
			 * 
			 * @see FileUploadCallback
			 */
			MAX_ITEM_SIZE,
			/**
			 * Item name regular expressions not matched
			 * 
			 * @see FileUploadCallback
			 */
			ITEM_TYPE
		}
		
	}

}

