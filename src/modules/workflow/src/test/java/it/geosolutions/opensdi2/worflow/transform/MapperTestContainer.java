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
package it.geosolutions.opensdi2.worflow.transform;

import java.util.Map;

public class MapperTestContainer {
	public Map<String, Object> getSource() {
		return source;
	}
	public void setSource(Map<String, Object> source) {
		this.source = source;
	}
	public Map<String, String> getRules() {
		return rules;
	}
	public void setRules(Map<String, String> rules) {
		this.rules = rules;
	}
	public Map<String,Object> getExpected() {
		return expected;
	}
	public void setExpected(Map<String,Object> expected) {
		this.expected = expected;
	}
	private Map<String,Object> source;
	private Map<String,String> rules;
	private Map<String,Object> expected;
	
	
	
	
}
