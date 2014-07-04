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
import freemarker.template.TemplateException;
import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserAttribute;
import it.geosolutions.geostore.core.model.enums.Role;
import it.geosolutions.opensdi2.email.OpenSDIMailer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test the email generation
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/applicationContext-test.xml")
public class TemplateTest {
	private final static Logger LOGGER = Logger.getLogger(TemplateTest.class);

	@Autowired
	OpenSDIMailer notificationService;
	
		 
	 /**
	  * test the expiring user functionality
	  */
	@Test
	public void generateEmailTest(){
		try {
			//TEST MESSAGE GENERATION
			String message = notificationService.applyTemplate("emptyMessage-test.ftl", null);
			LOGGER.info("email generated:\n" + message);
			
			//Create users
			Map<String,Object> model = new HashMap<String,Object>();
			List<User> users = createUsers();
			model.put("users",users);
			
			
			message = notificationService.applyTemplate("adminNotification-test.ftl", model);
			LOGGER.info("email generated:\n" + message);
			assertTrue(message.contains("user28@test.com"));
			// all even users has date
			assertTrue(message.contains("06/28/2014"));
			assertTrue(message.contains("GeoSolutions"));
			
		} catch (IOException e) {
			fail();
		} catch (TemplateException e) {
			fail();
		}
	}
	
	/**
	 * Utility method for create fake users for test
	 * @return
	 */
	private List<User> createUsers() {
		//Add fake email attribute
		List<User> users = new ArrayList<User>();
		//create users
		for(int i = 0 ; i < 30; i++){
			//set class attributes
			User u = new User();
			u.setId((long)i);
			u.setName("user"+i);
			u.setEnabled(true);
			u.setRole(Role.USER);
			
			//set user attributes
			List<UserAttribute> attr = u.getAttribute();
			if(attr ==null){
				attr =new ArrayList<UserAttribute>();
			}
			//add email
			UserAttribute email = new UserAttribute();
			email.setName("email");
			email.setValue(u.getName() + "@test.com");
			attr.add(email);
			//add expires date
			if(i % 2 == 0){
				UserAttribute attribute = new UserAttribute();
				attribute.setName("expires");
				attribute.setValue("06/"+i+"/2014");
				attr.add(attribute);
			}
			//add company
			if(i % 3 == 0){
				UserAttribute attribute = new UserAttribute();
				attribute.setName("company");
				attribute.setValue("GeoSolutions");
				attr.add(attribute);
			}
			//add notes
			if(i % 4 == 0){
				UserAttribute attribute = new UserAttribute();
				attribute.setName("notes");
				attribute.setValue("This is a sample note");
				attr.add(attribute);
			}
			
			
			u.setAttribute(attr);
			users.add(u);
		}
		//test missing attributes for first user
		users.get(0).setAttribute(null);
		return users;
	}

}
