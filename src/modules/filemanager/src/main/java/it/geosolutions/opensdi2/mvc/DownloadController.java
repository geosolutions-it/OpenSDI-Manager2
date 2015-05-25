/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.configurations.services.ConfigDepot;
import it.geosolutions.opensdi2.download.DownloadService;
import it.geosolutions.opensdi2.download.Order;
import it.geosolutions.opensdi2.download.order.ListOrder;
import it.geosolutions.opensdi2.download.register.OrderInfo;
import it.geosolutions.opensdi2.download.register.OrderStatus;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the base Download Service. Is more powerful that the simple
 * file browser because it allows to use a custom download service.
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * @author DamianoG
 *
 */
@Controller
@RequestMapping("/download")
public class DownloadController {

	private final static Logger LOGGER = Logger
			.getLogger(DownloadController.class);

	public static final String SCOPE_ID = "downloadService";
	public static final String DEFAULT_INSTANCE_ID = "default";

	@Autowired
	protected ConfigDepot depot;

	@SuppressWarnings("rawtypes")
	// for now only one download service is allowed.
	// it is not mandatory but demanded to the specific implementation
	@Autowired(required = false)
	protected DownloadService downloadService;

	/**
	 * Create an order for a list of elements
	 * @param order
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listorder", method = { RequestMethod.POST })
	public @ResponseBody OrderInfo download(
			@RequestBody ListOrder order, HttpServletRequest request) {

		if (downloadService == null) {
			throw new IllegalStateException(
					"The downloadService for the module '"
							+ DownloadController.class
							+ "' cannot be loaded...");
		}
		String id = downloadService.registerOrder(order);
		return downloadService.getOrderInfo(id);

	}

	@RequestMapping(value = "/get/{id}", method = { RequestMethod.GET })
	public void getOrder(@PathVariable(value = "id") String id,
			HttpServletRequest request, HttpServletResponse servletResponse) {

		if (downloadService == null) {
			throw new IllegalStateException(
					"The downloadService for the module '"
							+ DownloadController.class
							+ "' cannot be loaded...");
		}

		// check the order in the register
		OrderInfo entry = downloadService.getOrderInfo(id);

		if (entry == null || entry.getStatus() != OrderStatus.READY) {
			throw new IllegalStateException("The order is not completed yet");
		}

		Order order = entry.getOrder();
		if (order == null) {
			throw new IllegalStateException(
					"The downloadService needs An Order in the request body");
		}
		// TODO catch limits
		try {
			// set response headers
			servletResponse.setContentType("application/force-download");

			// response.setContentLength(-1);
			servletResponse.setHeader("Content-Transfer-Encoding", "binary");
			servletResponse.setHeader("Content-Disposition",
					"attachment; filename=\"" + "order-" + id + ".zip\"");
			downloadService.getDownload(order, servletResponse.getOutputStream());
			downloadService.getOrderInfo(id);
		} catch (IOException e) {
			LOGGER.error("error during ZIP file creation in the module "
					+ DownloadController.class, e);
			throw new IllegalStateException(
					"The downloadService produced an error", e);
		}
	}

	// SETTERS AND GETTERS
	public ConfigDepot getDepot() {
		return depot;
	}

	public void setDepot(ConfigDepot depot) {
		this.depot = depot;
	}

	public @SuppressWarnings("rawtypes") DownloadService getDownloadService() {
		return downloadService;
	}

	public void setDownloadService(
			@SuppressWarnings("rawtypes") DownloadService downloadService) {
		this.downloadService = downloadService;
	}

}
