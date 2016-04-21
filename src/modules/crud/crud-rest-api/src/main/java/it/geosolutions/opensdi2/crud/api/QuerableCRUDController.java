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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public interface QuerableCRUDController<T,FILTERTYPE> {
	/**
	 * Get a list of the search result
	 * @param query the query
	 * @return the search results
	 * @throws RESTControllerException
	 */
	@RequestMapping(value="/find")
	public @ResponseBody CRUDResponseWrapper<T> find( @RequestParam("filter") FILTERTYPE query ) throws RESTControllerException;
	
	/**
	 * Simple text search
	 * @param attName optional attribute name from where to apply. If null predefined text search
	 * @param valueLike the query string
	 * @return the search results
	 * @throws RESTControllerException
	 */
	@RequestMapping(value= "/filterby/", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody CRUDResponseWrapper<T> find( @RequestParam("attributename") String attName, @RequestParam("valuelike") String valueLike ) throws RESTControllerException;
}
