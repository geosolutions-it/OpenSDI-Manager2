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
package it.geosolutions.opensdi2.workflow.action;

import it.geosolutions.opensdi2.email.OpenSDIMailer;
import it.geosolutions.opensdi2.workflow.BlockConfiguration;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.PropertyAccessor;

/**
 * Configuration object for the Email Block.
 * 
 * 
 * @author lorenzo
 *
 */
public class EmailActionConfiguration implements BlockConfiguration{
	/**
	 * The notification Service
	 */
	 private OpenSDIMailer notificationService;
	
   /**
    * The input accessors for the email parameters
	*/
	private List<PropertyAccessor> inputPropertyAccessors;
	
	/**
	 * Rules to populate the <EmailActionContextConfiguration> object.
	 * NOTE: rules supports ONLY one level property access in write mode.
	 * 
	 */
	private Map<String,String>	rules;

	public OpenSDIMailer getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(OpenSDIMailer notificationService) {
		this.notificationService = notificationService;
	}

	
	public List<PropertyAccessor> getInputPropertyAccessors() {
		return inputPropertyAccessors;
	}

	public void setInputPropertyAccessors(List<PropertyAccessor> inputAccessors) {
		this.inputPropertyAccessors = inputAccessors;
	}

	public Map<String,String> getRules() {
		return rules;
	}

	public void setRules(Map<String,String> rules) {
		this.rules = rules;
	}
	
	
}
