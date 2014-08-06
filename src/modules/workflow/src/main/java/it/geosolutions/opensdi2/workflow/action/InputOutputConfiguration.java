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

import it.geosolutions.opensdi2.workflow.BlockConfiguration;

/**
 * Basic Workflow configuration class for blocks with an input and an output (both optionals).
 * 
 * @author Mauro Bartolomeoli
 *
 */
public class InputOutputConfiguration implements BlockConfiguration {
	/**
	 * identifier of the input object from context
	 */
	private String inputObjectId;
	/**
	 * identifier of the output object in context
	 */
	private String outputObjectId;
	
	public String getInputObjectId() {
		return inputObjectId;
	}
	public void setInputObjectId(String inputObjectId) {
		this.inputObjectId = inputObjectId;
	}
	public String getOutputObjectId() {
		return outputObjectId;
	}
	public void setOutputObjectId(String outputObjectId) {
		this.outputObjectId = outputObjectId;
	}
}
