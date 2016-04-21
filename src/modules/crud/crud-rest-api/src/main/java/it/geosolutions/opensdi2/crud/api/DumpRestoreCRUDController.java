/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Generic interface for dump and restore. Manage only a list of resources
 * @author Lorenzo Natali
 *
 * @param <T> the Type of the resource to dump/restore
 */
public interface DumpRestoreCRUDController<T> {
	
	/**
	 * Dump the resource
	 * @return the List of the resoruce
	 * @throws RESTControllerException
	 */
	@RequestMapping(value = "/dump", method = RequestMethod.GET)
	public @ResponseBody CRUDResponseWrapper<T> dump() throws RESTControllerException;
	
	/**
	 * Restore the list of resources
	 * @param lcd the list of resorces
	 * @return 
	 * @throws RESTControllerException
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody String restore(@RequestBody CRUDResponseWrapper<T> lcd) throws RESTControllerException;
}
