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

import it.geosolutions.opensdi2.workflow.BaseAction;
import it.geosolutions.opensdi2.workflow.BlockConfiguration;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.action.InputOutputConfiguration;

import java.io.IOException;

import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Action that reads a GeoJSON object from the configured input (default "input") and
 * stores a SimpleFeature object in the configured output (default "output").
 * 
 * Uses an optional InputOutputConfiguration object to get configuration.
 *  
 * @author Mauro Bartolomeoli
 *
 */
public class GeoJSONReader extends BaseAction {
	
	private FeatureJSON jsonReader = new FeatureJSON();
	
	private String inputId = "input";
	private String outputId = "output";
	
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		try {
			SimpleFeature feature = jsonReader.readFeature(ctx.getContextElement(inputId));
			ctx.addContextElement(outputId, feature);
		} catch (IOException e) {
			throw new WorkflowException("Error reading GeoJSON input", e);
		}
		
	}

	@Override
	public void setConfiguration(BlockConfiguration config) {
		super.setConfiguration(config);
		if(config != null && config instanceof InputOutputConfiguration) {
			InputOutputConfiguration ioCfg = (InputOutputConfiguration)config;
			if(ioCfg.getInputObjectId() != null) {
				inputId = ioCfg.getInputObjectId();
			}
			if(ioCfg.getOutputObjectId() != null) {
				outputId = ioCfg.getOutputObjectId();
			}
		}
	}

	
}
