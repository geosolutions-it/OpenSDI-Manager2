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
package it.getosolutions.opensdi2.workflow.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	/**
	 * get an attribute evaluating the attribute expression
	 * @param attribute
	 * @param obj the object to inspect
	 */
	@SuppressWarnings("unchecked")
	public static Object getAttribute(String attribute, Map<String,Object> obj){
		Object current= obj;
		for (String key : attribute.split("\\.")){
            if (key.length() > 0) {
            	if(current instanceof Map){
            		try{
            			current = ((Map<String,Object>)current).get(key);
            			//NULL or
            		}catch(Exception e){
            			throw new IllegalArgumentException(attribute + "is not parsable" + "access to \"" + key + "\" impossible");
            		}
            	}else{
            		return null;
            	}
	        }
		}
		return current;
	}
	/**
	 * Place the attribute in the Map. 
	 * If the path doesn't exist, it will be created
	 * @param attribute
	 * @param value
	 * @param obj
	 */
	public static void setAttribute(String attribute,Object value, Map<String,Object> obj){
		
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
		if((container!=null)){
        	container.put(attributes[attributes.length-1], value);
        } 
		
	}
}
