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
package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCoverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class HerbaceousRelativeCoverService extends BaseService<HerbaceousRelativeCover, Integer> {

    @Autowired
    private HerbaceousRelativeCoverDao herbaceousRelativeCoverDao;

    @Override
    protected GenericVibiDao<HerbaceousRelativeCover, Integer> getDao() {
        return herbaceousRelativeCoverDao;
    }
}
