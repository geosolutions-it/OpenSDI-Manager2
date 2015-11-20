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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ETj (etj at geo-solutions.it)
 * @author adiaz
 */
@Transactional(value = "opensdiTransactionManager")
public class CropDataDAOImpl extends BaseDAO<CropData, Long> implements
        CropDataDAO {

private final static Logger LOGGER = LoggerFactory.getLogger(CropDataDAOImpl.class);    
    
private String src = null;
    
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

private static String[] PKNames = { "cropDescriptor.id", "district", "province", "year", "src" };

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
protected Class<CropData> getEntityType() {
    return CropData.class;
}

// WORKAROUND Overriding these 2 methods in order to add src to the pk names... the value of src is not extracted from the CSV but passed from the interface
@Override
public CropData searchByPK(Serializable... pkObjects){
    return searchByPK(getPKNames(), addNewPKey(pkObjects, src));
}

@Override
public boolean removeByPK(Serializable... pkObjects){
    return removeByPK(getPKNames(), addNewPKey(pkObjects, src));
}

private Serializable[] addNewPKey(Serializable[] pkObjects, String src){
    if(src == null){
        throw new IllegalArgumentException("The datasource organization name is missing...");
    }
    if(pkObjects[pkObjects.length-1] == null){
        pkObjects[pkObjects.length-1] = src;
    }
    return pkObjects;
}

@Override
public void setSrc(String src) {
    this.src = src;
}

}
