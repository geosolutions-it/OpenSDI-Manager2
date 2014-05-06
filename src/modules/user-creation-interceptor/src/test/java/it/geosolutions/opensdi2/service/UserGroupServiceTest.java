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
package it.geosolutions.opensdi2.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for the LDAP user group service
 * 
 * @author adiaz
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/ldapTest-applicationContext.xml")
public class UserGroupServiceTest {
	
	private static Logger LOGGER = Logger.getLogger(UserGroupServiceTest.class);
	
	/**
	 * Service to be tested
	 */
	@Autowired
	UserGroupService userService;
	
	public static final String UID_SEARCH = "admin";
	
	/**
	 * Check if the list of files is correct
	 */
	@Test
	public void testFileList(){
		List<Map<String, Object>> users = userService.search(UID_SEARCH);
		for(Map<String, Object> user: users){
			assertNotNull(user);
			LOGGER.info("User --> " + user);
		}
	}
	
	@Test
	public void testAddGroups(){
		userService.setUserGroups(UID_SEARCH, null);
		List<String> groups = userService.getUserGroups(UID_SEARCH);
		assertEquals(groups.size(), 0);
		groups = new LinkedList<String>();
		groups.add("sn-manager");
		groups.add("va-sp");
		userService.setUserGroups(UID_SEARCH, groups);
		assertEquals(userService.getUserGroups(UID_SEARCH).size(), groups.size());
	}
	
	@Before
	public void cleanGroupsList(){
		userService.setUserGroups(UID_SEARCH, null);
	}

}
