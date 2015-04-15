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

import it.geosolutions.opensdi.model.CropStatus;
import it.geosolutions.opensdi.persistence.dao.CropStatusDAO;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author adiaz
 */
@Transactional(value = "opensdiTransactionManager")
public class CropStatusDAOImpl extends BaseDAO<CropStatus, Long> implements
        CropStatusDAO {

@Override
public void persist(CropStatus... entities) {
    super.persist(entities);
}

@Override
public CropStatus merge(CropStatus entity) {
    return super.merge(entity);
}

@Override
public boolean remove(CropStatus entity) {
    return super.remove(entity);
}

@Override
public boolean removeById(Long id) {
    return super.removeById(id);
}

private static String[] PKNames = { "crop", "month", "factor", "dec" };

/**
 * Obtain array for the pknames ordered to be used in
 * {@link BaseDAO#removeByPK(Serializable...)}
 * 
 * @return array with names of the pk aggregated
 */
public String[] getPKNames() {
    return PKNames;
}

@Override
protected Class<CropStatus> getEntityType() {
    return CropStatus.class;
}

}
