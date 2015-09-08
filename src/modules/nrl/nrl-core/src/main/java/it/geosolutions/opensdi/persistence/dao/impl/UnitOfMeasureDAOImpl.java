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

import it.geosolutions.opensdi.model.UnitOfMeasure;
import it.geosolutions.opensdi.persistence.dao.UnitOfMeasureDAO;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;

/**
 * @author Lorenzo Natali
 * @author ETj
 */
@Transactional(value = "opensdiTransactionManager")
public class UnitOfMeasureDAOImpl extends
        BaseDAO<UnitOfMeasure, String> implements UnitOfMeasureDAO {

@Override
public void persist(UnitOfMeasure... entities) {
    super.persist(entities);
}

@Override
public UnitOfMeasure merge(UnitOfMeasure entity) {
    return super.merge(entity);
}

@Override
public boolean remove(UnitOfMeasure entity) {
    return super.remove(entity);
}

@Override
public boolean removeById(String id) {
    return super.removeById(id); // To change body of generated methods, choose
                                 // Tools | Templates.
}



private static String[] PKNames = { "id" };

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
public List<UnitOfMeasure> getByClass(String valueLike) {
	Search search = new Search(persistentClass);
	search.addFilterEqual("cls", valueLike);
	return search(search);
}

@Override
protected Class<UnitOfMeasure> getEntityType() {
    return UnitOfMeasure.class;
}


}
