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
package it.geosolutions.opensdi2.mvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.TemplateException;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.opensdi2.email.OpenSDIMailer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Class that allows to create and send email for users
 * or generic from the selected templates
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
@Controller
@RequestMapping("/admin/mailer")
public class EMailController {
	private static final Logger LOGGER = Logger.getLogger(EMailController.class);
	@Autowired
	AdministratorGeoStoreClient administratorGeoStoreClient;
	
	@Autowired
	OpenSDIMailer notificationService;
	/**
	 * Apply the template for an email using the user in the 
	 * template model
	 * @param template the template name
	 * @param username the name of the user to use for the model
	 * @return the message body
	 */
	@RequestMapping(value = "/user/{template}/{username}/body", method=RequestMethod.GET)
	public @ResponseBody String getMessageBody(@PathVariable String template,@PathVariable String username){
		
		Map<String,Object> model = new HashMap<String,Object>();
		//TODO get the user with the username.
		model.put("user",username);
		String messageBody ="";
		try {
			messageBody = notificationService.applyTemplate(template, model);
		} catch (IOException e) {
			//TODO error response to improve
			messageBody = "could't get the template:" + template+ ". " +e.getMessage() ;
			LOGGER.error(messageBody);
		} catch (TemplateException e) {
			//TODO error response to improve
			messageBody = "could't generate the body message from the template:" + template+ ". " +e.getMessage();
			LOGGER.error(messageBody);
			
		}
		//Map<String,Object> ret = new HashMap<String,Object>();
		//ret .put("body", messageBody);
		return messageBody;
	}
	
	
}
