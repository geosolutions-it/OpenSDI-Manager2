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

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the needed properties to send an email using a 
 * freemarker template available in the context
 * @author lorenzo
 *
 */
public class EmailActionContextConfiguration {
	private String from;
	private String to;
	private String template;
	@SuppressWarnings("rawtypes")
	private Map model =new HashMap<String,Object>();
	private String subject;

	public String getFrom() {
		return from;
	}
	
	/**
	 * Set the address where the email is from
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	
	/**
	 * Set the address of the email receiver
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}
	public String getTemplate() {
		return template;
	}
	
	/**
	 * Set the Freemarker template name to use to send the email
	 * @param template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public Map getModel() {
		return model;
	}
	
	/**
	 * Set the model object(s) for the template.
	 * 
	 * @param model
	 */
	public void setModel(@SuppressWarnings("rawtypes") Map model) {
		this.model = model;
	}

	public String getSubject() {
		return subject;
	}

	/**
	 * Set the email subject
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
