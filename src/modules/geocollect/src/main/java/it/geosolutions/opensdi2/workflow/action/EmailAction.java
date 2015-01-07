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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import freemarker.template.TemplateException;

import it.geosolutions.opensdi2.email.OpenSDIMailer;
import it.geosolutions.opensdi2.workflow.BaseAction;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.transform.spel.SpelTransformer;
import it.geosolutions.opensdi2.workflow.transform.spel.accessors.MapPropertyAccessor;
/**
 * Email Action for workflows. 
 * For now it uses the default <OpenSDI2Mailer> autowired as notificationService.
 * TODO In the future we configure which service use from the configuration 
 * @author lorenzo
 *
 */
public class EmailAction extends BaseAction {
	private final static Logger LOGGER = Logger
			.getLogger(EmailAction.class);
	private static final List<PropertyAccessor> outputPropertyAccessors = Arrays.asList((PropertyAccessor)new ReflectivePropertyAccessor(),new MapPropertyAccessor());
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		
		EmailActionConfiguration config = (EmailActionConfiguration) getConfiguration();
		
		// create a transform action
		SpelTransformer<WorkflowContext, EmailActionContextConfiguration> tr =  new SpelTransformer<WorkflowContext,EmailActionContextConfiguration>();
		// create the output object
		EmailActionContextConfiguration cc = new EmailActionContextConfiguration();
		
		//set property accessors for input and output
		tr.setInputaccessors(config.getInputPropertyAccessors());
		tr.setOutputaccessors(outputPropertyAccessors);
		tr.setRules(config.getRules());
		tr.setOutputObject(cc);
		cc = tr.transform(ctx);
		OpenSDIMailer mailer = config.getNotificationService();
		String messageBody;
		try {
			messageBody = mailer.applyTemplate(cc.getTemplate(), cc.getModel());
		} catch (IOException e) {
			LOGGER.error("Unable to read the template for the email",e);
			throw new WorkflowException(e);
		} catch (TemplateException e) {
			LOGGER.error("Unable to apply the template for the email",e);
			throw new WorkflowException(e);
		}
		mailer.sendMail(cc.getFrom(), cc.getTo(), cc.getSubject(), messageBody);
		
	}

}
