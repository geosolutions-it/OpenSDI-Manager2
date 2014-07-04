/*
 *  OpenSDI Manager
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
/**
 * 
 */
package it.geosolutions.opensdi2.userexpiring;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import freemarker.template.TemplateException;
import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserAttribute;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTUser;
import it.geosolutions.geostore.services.rest.model.UserList;
import it.geosolutions.opensdi2.email.OpenSDIMailer;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test the user expiring
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContext-test.xml")
public class NotificationTest {
	private final static Logger LOGGER = Logger
			.getLogger(NotificationTest.class);

	private AdministratorGeoStoreClient client;

	@Autowired
	AdministratorGeoStoreClient administratorGeoStoreClient;

	@Autowired
	OpenSDIMailer notificationService;

	@Before
	public void before() throws Exception {
		client = getClient();
		// if missing, we can not continue
		assumeTrue(pingGeoStore(client));

	}

	/**
	 * Create the client for the test
	 * 
	 * @return
	 */
	protected AdministratorGeoStoreClient getClient() {
		return administratorGeoStoreClient;
	}

	/**
	 * test the expiring user functionality
	 */
	@Test
	public void generateEmailTest() {
		try {
			// TEST MESSAGE GENERATION
			String message = notificationService.applyTemplate(
					"emptyMessage-test.ftl", null);
			LOGGER.info("email generated:\n" + message);

			// TEST USERS FROM GEOSTORE
			UserList ul = administratorGeoStoreClient.getUsers(0, 20);

			List<User> users = new ArrayList<User>();
			for (RESTUser user : ul.getList()) {
				users.add(administratorGeoStoreClient.getUser(user.getId(),
						true));
			}

			assertTrue(users.size() > 0);
			for (User u : users) {
				LOGGER.info("*******************");
				LOGGER.info("name:" + u.getName());
				LOGGER.info("id:" + u.getId());

				if (u.getAttribute() != null) {
					LOGGER.info("attributes(" + u.getAttribute().size() + ")");
					for (UserAttribute ua : u.getAttribute()) {
						LOGGER.info(ua.getName() + ":" + ua.getValue());
					}
				}
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("users", users);
			message = notificationService.applyTemplate(
					"adminNotification-test.ftl", model);

			LOGGER.info(" ****** Generated message  ****** \n" + message);
		} catch (IOException e) {
			fail();
		} catch (TemplateException e) {
			fail();
		}
	}

	/**
	 * check GeoStore to see if the test can run
	 * 
	 * @param client
	 * @return
	 */
	protected boolean pingGeoStore(GeoStoreClient client) {
		try {
			client.getCategories();
			return true;
		} catch (Exception ex) {
			LOGGER.error("Error connecting geostore", ex);
			// ... and now for an awful example of heuristic.....
			Throwable t = ex;
			while (t != null) {
				if (t instanceof ConnectException) {
					LOGGER.warn("Testing GeoStore is offline");
					return false;
				}
				t = t.getCause();
			}

		}
		return false;
	}
}
