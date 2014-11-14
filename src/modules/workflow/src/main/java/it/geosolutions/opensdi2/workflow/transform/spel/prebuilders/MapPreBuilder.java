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
package it.geosolutions.opensdi2.workflow.transform.spel.prebuilders;


import it.geosolutions.opensdi2.workflow.transform.PreBuilder;

import java.util.HashMap;
import java.util.Map;


public class MapPreBuilder implements PreBuilder<Map<String,Object>> {

	@Override
	public Map<String, Object> build(Map<String, String> rules) {
		Map<String, Object> out = new HashMap<String, Object>();
		return build(out,rules);
	}
	private void preparePath(String attribute,Map<String, Object> obj){
		
		Map<String,Object> container = obj; 
		//find the last container, creating the intermediate maps
		String[] attributes = attribute.split("\\.");
		for (int i = 0; i<attributes.length -1  ;i++){
			String key  = attributes[i];
            if (key.length() > 0) {
            	//create the path of Maps if doesn't exist
            	Object current = container.get(key);
            	if(current == null){
            		container.put(key,new HashMap<String,Object>());
            		current = container.get(key);
            	}
            	//update reference to the container
            	if(( current instanceof Map ) && !"".equals(key) ){
            		container = (Map<String,Object>) current;
            	}
        	}
		}
	}
	@Override
	public Map<String, Object> build(Map<String, Object> outputObject,
			Map<String, String> rules) {
		if(rules !=null){
			for(String rule : rules.keySet()){
				preparePath(rule, outputObject);
			}
		}
		return outputObject;

	}
}
