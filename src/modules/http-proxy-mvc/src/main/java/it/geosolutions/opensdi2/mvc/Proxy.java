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

import it.geosolutions.httpproxy.service.ProxyService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Proxy controller for OpenSDI-Manager2
 * 
 * @author adiaz
 * 
 */
@Controller
@RequestMapping("/proxy")
public class Proxy {

    @Resource(name = "proxyService")
    protected ProxyService proxy;

    private final static Logger LOGGER = Logger.getLogger(Proxy.class);

    /**
     * Handle proxy reqquest.
     * 
     * @param request servlet request
     * @param response servlet response
     * 
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calling proxy");
        }

        proxy.execute(request, response);

    }

}
