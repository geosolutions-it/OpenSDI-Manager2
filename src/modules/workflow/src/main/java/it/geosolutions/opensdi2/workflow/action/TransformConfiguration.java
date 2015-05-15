/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 - 2015 GeoSolutions S.A.S.
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

import java.util.List;
import java.util.Map;

import org.springframework.expression.PropertyAccessor;

import it.geosolutions.opensdi2.workflow.transform.PreBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Utility container to configure the Transform Action
 * @author lorenzo
 *
 */
public class TransformConfiguration extends InputOutputConfiguration{
	
	private List<PropertyAccessor> inputPropertyAccessor;
	private List<PropertyAccessor> outputPropertyAccessor;
	private Map<String,String> rules;
	@JsonIgnore
	@SuppressWarnings("rawtypes")
	private PreBuilder preBuilder;
	
	public List<PropertyAccessor> getInputPropertyAccessor() {
		return inputPropertyAccessor;
	}
	public void setInputPropertyAccessor(List<PropertyAccessor> inputPropertyAccessor) {
		this.inputPropertyAccessor = inputPropertyAccessor;
	}
	public List<PropertyAccessor> getOutputPropertyAccessor() {
		return outputPropertyAccessor;
	}
	public void setOutputPropertyAccessor(List<PropertyAccessor> outputProperyAccessor) {
		this.outputPropertyAccessor = outputProperyAccessor;
	}
	public PreBuilder getPreBuilder() {
		return preBuilder;
	}
	public void setPreBuilder(PreBuilder preBuilder) {
		this.preBuilder = preBuilder;
	}
	
	public Map<String,String> getRules() {
		return rules;
	}
	public void setRules(Map<String,String> rules) {
		this.rules = rules;
	}
	
	
}
