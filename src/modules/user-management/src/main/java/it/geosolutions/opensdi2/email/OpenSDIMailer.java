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
package it.geosolutions.opensdi2.email;

import java.io.IOException;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

public class OpenSDIMailer {
	private static final Logger LOGGER = Logger.getLogger(OpenSDIMailer.class);
	@Autowired
	private MailSender mailSender;

	@Autowired
	private Configuration configuration;

	public void sendMail(String from, String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}

	/**
	 * Useful class to get freemarker template from
	 * 
	 * @param template
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String applyTemplate(String template,
			@SuppressWarnings("rawtypes") Map model) throws IOException,
			TemplateException {
		String result = null;
		try {
			result = FreeMarkerTemplateUtils.processTemplateIntoString(
					configuration.getTemplate(template), model);
		} catch (IOException e) {
			LOGGER.error("unable to read template file" + template, e);
			throw e;

		} catch (TemplateException e) {
			LOGGER.error("unable to use freMarkerTemplate:" + template);
			throw e;
		}
		return result;
	}

	// GETTERS AND SETTERS
	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
}
