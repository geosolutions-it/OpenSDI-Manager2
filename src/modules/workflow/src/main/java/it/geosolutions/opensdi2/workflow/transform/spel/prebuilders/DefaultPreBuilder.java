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

import java.util.Map;

import org.apache.log4j.Logger;
/**
 * A default pre-builder. Can be used if the objects doesn't require
 * initialization and have only to be created.
 * @author lorenzo
 *
 * @param <DESTTYPE>
 */
public class DefaultPreBuilder<DESTTYPE> implements PreBuilder<DESTTYPE> {
	private final static Logger LOGGER = Logger.getLogger(DefaultPreBuilder.class);
	private Class<DESTTYPE> clazz;

	public DefaultPreBuilder(Class<DESTTYPE> clazz){
		this.clazz = clazz;
	}
	@Override
	public DESTTYPE build(Map<String, String> rules) {
		try {
			return  (DESTTYPE)clazz.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("unable to instantiate class"+ clazz,e);
		} catch (IllegalAccessException e) {
			LOGGER.error("unable to instantiate class"+ clazz,e);
		}
		return null;
	}
	@Override
	public DESTTYPE build(DESTTYPE outputObject,
			Map<String, String> rules) {
		return outputObject;
	}
}
	