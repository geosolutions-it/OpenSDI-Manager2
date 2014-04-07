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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.service.URLFacade;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * URL wrapper for GeoStore on OpenSDI-Manager2
 * 
 * @author adiaz
 * 
 */
@Controller
@RequestMapping("/geostore")
public class GeostoreWrapper {

	@Resource(name="geostoreFacade")
	protected URLFacade urlFacade;

	private final static Logger LOGGER = Logger.getLogger(GeostoreWrapper.class);
	
	@RequestMapping(value = "/{gotParam1}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}/{gotParam3}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
	        @PathVariable(value = "gotParam3") String gotParam3,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2, gotParam3);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}/{gotParam3}/{gotParam4}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
	        @PathVariable(value = "gotParam3") String gotParam3,
	        @PathVariable(value = "gotParam4") String gotParam4,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2, gotParam3, gotParam4);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}/{gotParam3}/{gotParam4}/{gotParam5}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
	        @PathVariable(value = "gotParam3") String gotParam3,
	        @PathVariable(value = "gotParam4") String gotParam4,
	        @PathVariable(value = "gotParam5") String gotParam5,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2, gotParam3, gotParam4, gotParam5);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}/{gotParam3}/{gotParam4}/{gotParam5}/{gotParam6}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
	        @PathVariable(value = "gotParam3") String gotParam3,
	        @PathVariable(value = "gotParam4") String gotParam4,
	        @PathVariable(value = "gotParam5") String gotParam5,
	        @PathVariable(value = "gotParam6") String gotParam6,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2, gotParam3, gotParam4, gotParam5, gotParam6);
		
	}
	
	@RequestMapping(value = "/{gotParam1}/{gotParam2}/{gotParam3}/{gotParam4}/{gotParam5}/{gotParam6}/{gotParam7}")
	public void callGeostore(
	        @PathVariable(value = "gotParam1") String gotParam1,
	        @PathVariable(value = "gotParam2") String gotParam2,
	        @PathVariable(value = "gotParam3") String gotParam3,
	        @PathVariable(value = "gotParam4") String gotParam4,
	        @PathVariable(value = "gotParam5") String gotParam5,
	        @PathVariable(value = "gotParam6") String gotParam6,
	        @PathVariable(value = "gotParam7") String gotParam7,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling proxy");
		}

		urlFacade.handleRequest(request, response, gotParam1, gotParam2, gotParam3, gotParam4, gotParam5, gotParam6, gotParam7);
		
	}

}
