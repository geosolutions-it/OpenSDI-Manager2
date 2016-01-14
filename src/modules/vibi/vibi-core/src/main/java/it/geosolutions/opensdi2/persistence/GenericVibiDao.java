/*
 *  VIBI
 *  https://github.com/geosolutions-it/vibi
 *  Copyright (C) 2015 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.persistence;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Sort;
import org.hibernate.metadata.ClassMetadata;

import java.io.Serializable;
import java.util.List;

public interface GenericVibiDao<T, ID extends Serializable> extends
        GenericDAO<T, ID> {

    boolean removeByPK(Serializable... pkObjects);

    boolean removeByPK(String[] names, Serializable... pkObjects);

    String[] getPKNames();

    T searchByPK(Serializable... pkObjects);

    T searchByPK(String[] names, Serializable... pkObjects);

    ClassMetadata getClassMetadata();

    String[] getAllPropertiesNames();

    Filter getKeyWordSearchFilter(String keyword);

    Filter getPropertiesFilter(String filtersString);

    List<Sort> getPropertiesSorting(String sortingString);

    Class getEntityType();

    void persist(T entity);
}
