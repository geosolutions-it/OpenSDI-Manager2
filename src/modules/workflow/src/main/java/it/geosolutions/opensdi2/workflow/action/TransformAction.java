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

import java.awt.IllegalComponentStateException;

import it.geosolutions.opensdi2.workflow.BaseAction;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.getosolutions.opensdi2.workflow.transform.spel.SpelObjectTranformer;
/**
 * Transform objects using SpelObjectTransformer configurable by the confiuration object
 * @author lorenzo
 *
 */
public class TransformAction extends BaseAction{
	
	
	@Override
	public void executeAction(WorkflowContext ctx) {
		//configure a new transformer for execution
		SpelObjectTranformer t = new SpelObjectTranformer();
		if(getConfiguration() == null){
			throw new IllegalComponentStateException();
		}
		configureTransformer(t);
		
		TransformConfiguration config = (TransformConfiguration) getConfiguration();
		if(config !=null){
			Object input =null;
			//set input object to the configured one or with the context
			if(config.getInputObjectId()!=null){
				input = ctx.getContextElement(config.getInputObjectId());
			}else{
				input = ctx;
			}
			t.setOutputObject(ctx.getContextElement(config.getOutputObjectId()));
			Object output = t.transform(input);
			ctx.addContextElement(config.getOutputObjectId(), output);
		}
	}

	/**
	 * Configure the tranformer with the configuration object
	 * @param t the transformer to configure
	 */
	private void configureTransformer(SpelObjectTranformer t) {
		TransformConfiguration config = (TransformConfiguration) getConfiguration();
		if(config!=null){
			t.setInputaccessors(config.getInputPropertyAccessor());
			t.setOutputaccessors(config.getOutputPropertyAccessor());
			t.setOutputPreBuilder(config.getPreBuilder());
			t.setRules(config.getRules());
		}
	}


	
	
}
