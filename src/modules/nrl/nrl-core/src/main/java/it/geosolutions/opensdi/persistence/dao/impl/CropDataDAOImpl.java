/*
 *  nrl Crop Information Portal
 *  https://github.com/geosolutions-it/crop-information-portal
 *  Copyright (C) 2013 GeoSolutions S.A.S.
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

package it.geosolutions.opensdi.persistence.dao.impl;

import it.geosolutions.opensdi.model.CropData;
import it.geosolutions.opensdi.persistence.dao.CropDataDAO;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author ETj (etj at geo-solutions.it)
 * @author adiaz
 */
@Transactional(value = "opensdiTransactionManager")
public class CropDataDAOImpl extends BaseDAO<CropData, Long> implements
        CropDataDAO {

@Override
public void persist(CropData... entities) {
    super.persist(entities);
}

@Override
public CropData merge(CropData entity) {
    return super.merge(entity);
}

@Override
public boolean remove(CropData entity) {
    return super.remove(entity);
}

@Override
public boolean removeById(Long id) {
    return super.removeById(id);
}

private static String[] PKNames = { "cropDescriptor.id", "district", "province", "year" };

/**
 * Obtain array for the pknames ordered to be used in
 * {@link BaseDAO#removeByPK(Serializable...)}
 * 
 * @return array with names of the pk aggregated
 */
public String[] getPKNames() {
    return PKNames;
}

}
