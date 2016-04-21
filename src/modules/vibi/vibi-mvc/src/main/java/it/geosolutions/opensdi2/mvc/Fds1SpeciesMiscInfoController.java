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

import it.geosolutions.opensdi2.persistence.Fds1SpeciesMiscInfo;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.Fds1SpeciesMiscInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/fds1SpeciesMiscInfo")
public class Fds1SpeciesMiscInfoController extends BaseController<Fds1SpeciesMiscInfo, String> {

    @Autowired
    Fds1SpeciesMiscInfoService fds1SpeciesMiscInfoService;

    @Override
    protected BaseService<Fds1SpeciesMiscInfo, String> getBaseService() {
        return fds1SpeciesMiscInfoService;
    }
}
