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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.CoverMidpointLookup;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.CoverMidpointLookupService;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/coverMidpointLookup")
public class CoverMidpointLookupController extends BaseController<CoverMidpointLookup, Integer> {

    @Autowired
    CoverMidpointLookupService coverMidpointLookupService;

    @Override
    protected BaseService<CoverMidpointLookup, Integer> getBaseService() {
        return coverMidpointLookupService;
    }
}
