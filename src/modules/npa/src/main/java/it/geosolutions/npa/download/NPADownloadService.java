package it.geosolutions.npa.download;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import it.geosolutions.npa.service.impl.JDBCUSIDService;
import it.geosolutions.opensdi2.download.Order;
import it.geosolutions.opensdi2.download.order.ListOrder;
import it.geosolutions.opensdi2.download.register.OrderStatus;
import it.geosolutions.opensdi2.download.services.ZipService;

/**
 * NPA Portal Implementation of download service.
 * 
 * @author Lorenzo Natali
 *
 */
public class NPADownloadService extends ZipService implements InitializingBean {

	private static final Logger LOGGER = Logger.getLogger(ZipService.class);

	// CONSTANTS
	private static final String DOWNLOAD_CONFIG_FILE = "download.xml";
	private static final String DOWNLOAD_BASE_ENTRY = "downloadBase";
	private static final String RULES_ENTRY = "rules";
	private static final String RULE_SEPARATOR = ";";
	private static final String RULE_FILE_DIRECTORY_SEPARATOR = ",";

	JDBCUSIDService usidService;

	@Override
	public String registerOrder(Order order) {
		// TODO Auto-generated method stub
		return getOrderRegister().registrer(order, OrderStatus.READY);
	}
	
	@Override
	public List<String> getFiles(String order) {
		Properties config = null;
		try {
			File configDir = usidService.getConfigRoot();
			checkORCreateConfigFile(null, configDir, DOWNLOAD_CONFIG_FILE);
			FileInputStream is = new FileInputStream(checkORCreateConfigFile(
					null, configDir, DOWNLOAD_CONFIG_FILE));
			config = new Properties();

			config.loadFromXML(is);
		} catch (InvalidPropertiesFormatException e) {
			throw new IllegalArgumentException(
					"unable to get the configuration file for download service",
					e);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"unable to get the configuration file for download service",
					e);
		}
		String rulesString = config.getProperty(RULES_ENTRY);
		List<String> files = new ArrayList<String>();
		String[] rules = rulesString.split(RULE_SEPARATOR);
		ExpressionParser parser = new SpelExpressionParser();
		for (String rule : rules) {
			String[] fileDirRule = rule.split(RULE_FILE_DIRECTORY_SEPARATOR);
			Expression exp = parser.parseExpression(fileDirRule[0]);
			EvaluationContext context = new StandardEvaluationContext(order);
			String name = (String) exp.getValue(context);
			String dir = null;
			if(fileDirRule.length >1){
				Expression expDir = parser.parseExpression(fileDirRule[1]);
				dir = (String) expDir.getValue(context);
			}
			//Get the list of accessible files for the rule
			List<File> f = this.getFiles(
					config.getProperty(DOWNLOAD_BASE_ENTRY), name,dir);
			for (File file : f) {
				files.add(convertToString(
						config.getProperty(DOWNLOAD_BASE_ENTRY), file));
			}
		}
		return files;
	}

	/**
	 * Convert the order to a string to create a proper structured zip file
	 * 
	 * Uses the download base to start creating the folder for each element
	 * 
	 * @param downloadBase
	 * @param file
	 * @return
	 */
	private String convertToString(String downloadBase, File file) {
		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath;
		try {
			zipFilePath = file.getCanonicalPath().substring(
					new File(downloadBase).getCanonicalPath().length() + 1,
					file.getCanonicalPath().length());
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"unable to get the canonical name of file:\n"
							+ file.getAbsolutePath() + " \ndirectory:"
							+ downloadBase, e);
		}
		return zipFilePath;

	}

	/**
	 * Get the files using the regex
	 * 
	 * @param downloadBase
	 *            base directory
	 * @param rule
	 *            the regex for file
	 * @param dirRule optional rule for directories
	 * @return the list of files that match the regex in the directory
	 */
	private List<File> getFiles(String downloadBase, String rule,String dirRule) {
		File dir = new File(downloadBase);
		List<File> files = new ArrayList<File>();
		
		files.addAll(FileUtils.listFiles(dir, new RegexFileFilter(rule),
				dirRule != null ? new RegexFileFilter(dirRule) :DirectoryFileFilter.DIRECTORY ));

		return files;
	}
	
	public List<String> getFiles(Collection<String> orders){
		List<String> ret = new ArrayList<String>();
		for (String s : orders) {
			
			// add some files to the zip
			ret.addAll(getFiles(s));

		}
		return ret;
	}

	@Override
	protected void downloadOrder(Order order, ZipOutputStream zipOut) {
		try {
			if (order instanceof ListOrder) {
				downloadOrder((ListOrder) order, zipOut);
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
	private void downloadOrder(ListOrder order, final ZipOutputStream zipOut) throws IOException{
		List<String> orderList = order.getOrder();
		LOGGER.info("generating zip file for order:" + order);
		LOGGER.debug("******* File List *******");
		try {
			Set<String> fileSet = new HashSet<String>();
			for (String s : orderList) {
				LOGGER.debug("ORDER:" + s);
				// add some files to the zip
				for (String file : getFiles(s)) {
					fileSet.add(file);
				}

			}for(String sfile : fileSet){
				addFileToZip(zipOut, sfile);
			}
		} catch (IOException e1) {
			LOGGER.error("Error during download creation", e1);
			throw e1;
		}catch (Exception e){
			LOGGER.error("Error during download creation", e);
		} finally {
			zipOut.close();
		}
	}

	private void addFileToZip(final ZipOutputStream zipOut, String file)
			throws IOException {
		FileInputStream fis = new FileInputStream(new File(getDownloadBase(),file));
		LOGGER.debug("file:" + file);
		ZipEntry e = new ZipEntry(file);
		zipOut.putNextEntry(e);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		zipOut.closeEntry();
	}

	public JDBCUSIDService getUsidService() {
		return usidService;
	}

	public void setUsidService(JDBCUSIDService usidService) {
		this.usidService = usidService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// load the configuration file from the npa directory
		File configDir = usidService.getConfigRoot();
		checkORCreateConfigFile(null, configDir, DOWNLOAD_CONFIG_FILE);

	}

	/**
	 * Check or create the properties file for the file
	 * 
	 * @param fileName
	 * @param namedRoot
	 * @param defaultResource
	 * @return
	 * @throws IOException
	 */
	protected File checkORCreateConfigFile(String fileName, File namedRoot,
			String defaultResource) throws IOException {

		fileName = fileName != null ? fileName : defaultResource;
		File file = new File(fileName);
		if (file.isAbsolute() == false)
			file = new File(namedRoot, fileName);

		if (file.exists())
			return file; // we are happy

		// try to find a template with the same name
		URL url = this.getClass().getResource(fileName);
		if (url != null)
			FileUtils.copyURLToFile(url, file);
		else
			// use the default template
			FileUtils.copyURLToFile(getClass().getResource(defaultResource),
					file);

		return file;

	}
	public String getDownloadBase(){
		Properties config = null;
		try {
			File configDir = usidService.getConfigRoot();
			checkORCreateConfigFile(null, configDir, DOWNLOAD_CONFIG_FILE);
			FileInputStream is = new FileInputStream(checkORCreateConfigFile(
					null, configDir, DOWNLOAD_CONFIG_FILE));
			config = new Properties();

			config.loadFromXML(is);
		} catch (InvalidPropertiesFormatException e) {
			throw new IllegalArgumentException(
					"unable to get the configuration file for download service",
					e);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"unable to get the configuration file for download service",
					e);
		}
		return config.getProperty(DOWNLOAD_BASE_ENTRY);
	}
}
