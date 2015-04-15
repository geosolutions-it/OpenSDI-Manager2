/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.nrl.cip.model;

import it.geosolutions.opensdi.persistence.dao.impl.BaseDAO;
import it.geosolutions.opensdi.persistence.dao.impl.CropDataDAOImpl;
import it.geosolutions.opensdi.persistence.dao.impl.FertilizerDAOImpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class UniqueFieldsTest extends Assert{

    @Test
    public void getCropDataUniqueFieldsTest(){
        
        BaseDAO cropDataDAO = new CropDataDAOImpl();
        assertEquals(5,cropDataDAO.getPKNames().length);
    }
    
    @Test
    public void getUniqueFieldsTest(){
        
        BaseDAO fertilizerDAO = new FertilizerDAOImpl();
        assertEquals(5,fertilizerDAO.getPKNames().length);
    }
    
    @Test
    public void getNoEntityUniqueFieldsTest(){
        
        BaseDAO testDAO = new BaseDAO<String, Long>() {

            @Override
            public String[] getPKNames() {
                return getUniqueFields();
            }

            @Override
            protected Class getEntityType() {
                return String.class;
            }
        };
        
        assertEquals(0,testDAO.getPKNames().length);
    }
    
}
