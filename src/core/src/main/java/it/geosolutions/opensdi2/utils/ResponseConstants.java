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
package it.geosolutions.opensdi2.utils;

/**
 * Response constants for JSON responses
 * 
 * @author adiaz
 *
 */
public class ResponseConstants {

	/**
	 * SUCCESS key. Possible values for this key is <code>true</code> or <code>false</code> 
	 */
	public static final String SUCCESS = "success";
	/**
	 * RESULTS key. Common response for this key is the number of records on {@link ResponseConstants#ROOT} 
	 */
	public static final String RESULTS = "results";
	/**
	 * ROOT key. This key is used for the data at response.
	 */
	public static final String ROOT = "root";
	/**
	 * DATA key. This key is used for the data at response.
	 */
	public static final String DATA = "data";
	/**
	 * COUNT key. Common response for this key is the number of records on {@link ResponseConstants#DATA}
	 */
	public static final String COUNT = "count";
	
}
