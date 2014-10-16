/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.config;

import java.io.Serializable;
import java.util.List;

/**
 * Base configuration for OpenSDI-Manager2 This bean could centralize all common
 * configuration
 * 
 * @author adiaz
 * 
 */
public class OpenSDIManagerConfigImpl implements OpenSDIManagerConfigProxy,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4107456302675180556L;

	public Object getConfiguration(String scope, Class extensionPoint) {
		List<Object> scopes = OpenSDIManagerConfigExtensions.extensions(scope);

		if (scopes != null && !scopes.isEmpty()) {
			// TODO: extension priority
			return scopes.get(0);
		}

		return null;
	}
}
