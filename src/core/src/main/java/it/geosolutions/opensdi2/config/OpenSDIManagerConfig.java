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

import java.io.File;


/**
 * Base configuration directory handler for OpenSDI-Manager2
 * The implementation classes are responsible to provide to the clients the location of the config dir and to load the config dir. 
 * 
 * @author adiaz
 * @author DamianoG
 *
 */
public interface OpenSDIManagerConfig {

	/**
	 * @return The File representing the root of the OpenSDI-Manager2 config dir
	 */
	public File getConfigDir();
	
	/**
	 * This method is responsible for the creation of the File pointer to the config dir. 
	 */
	public void initConfigDir();

}
