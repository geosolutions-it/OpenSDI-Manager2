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
package it.geosolutions.opensdi2.workflow.transform.spel.accessors;

import java.util.Map;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
/**
 * Accessor for Map<String,Object>.
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 *
 */
public class MapPropertyAccessor implements PropertyAccessor{

	@SuppressWarnings("unchecked")
	@Override
	public void write(EvaluationContext ctx, Object target, String name,
			Object value) throws AccessException {	
		if(target instanceof Map<?,?>) {
			((Map<String,Object>)target).put(name, value);
		}
	}

	@Override
	public TypedValue read(EvaluationContext ctx, Object target, String name)
			throws AccessException {
		if(target instanceof Map) {
			
			return new TypedValue(((Map<String,Object>) target).get(name));
		}
		return null;
	}

	@Override
	public Class[] getSpecificTargetClasses() {					
		return new Class[] {Map.class};
	}

	@Override
	public boolean canWrite(EvaluationContext ctx, Object target, String name)
			throws AccessException {					
		return true;
	}

	@Override
	public boolean canRead(EvaluationContext ctx, Object target, String name)
			throws AccessException {
		return target instanceof Map;
	}
}
