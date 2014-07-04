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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.userexpiring.model.UserExpiringStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to check status of the task
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@Controller
@RequestMapping("/admin/tasks")
public class TaskStatusController {
		
	@Autowired
	UserExpiringStatus userExpiringTaskStatus;
	/**
	 * Get the status of the task
	 * @return
	 */
	@RequestMapping(value = "/status", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody Object getStatus() {
		return userExpiringTaskStatus;
	}
	/** Reset the status message **/
	@RequestMapping(value = "/resetMessage", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody void resetMessage() {
		userExpiringTaskStatus.setMessage(null);
	}
	
}
