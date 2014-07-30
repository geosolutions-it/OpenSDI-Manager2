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
package it.geosolutions.opensdi2.workflow;

import java.util.HashMap;
import java.util.Map;
/**
 * The execution context for the workflow
 * @author lorenzo
 *
 */
public class WorkflowContext {
	/**
	 * Status Elements, TODO manage them to get the status of the workflow
	 */
	private Map<String,StatusElement> statusElements= new HashMap<String,StatusElement>();
	/**
	 * Context Elements, this contains the status of the flow
	 */
	private Map<String,Object> contextElements = new HashMap<String,Object>();
	
	public Map<String, StatusElement> getStatusElements() {
		return statusElements;
	}
	public void addStatusElement(String name,StatusElement e){
		statusElements.put(name,e);
	}
	public void addContextElement(String name,Object o){
		contextElements.put(name, o);
	}
	public Object getContextElement(String name){
		return contextElements.get(name);
	}
	public Map<String,Object> getContextElements(){
		return contextElements;
	}
}
