/**
 *
 */
package it.geosolutions.opensdi.service;

import it.geosolutions.opensdi.model.UnitOfMeasure;
import it.geosolutions.opensdi.persistence.dao.UnitOfMeasureDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional(value = "opensdiTransactionManager")
public class UnitOfMeasureService {

    @Autowired
    private UnitOfMeasureDAO unitOfMeasureDao;

    

	public UnitOfMeasureDAO getUnitOfMeasureDao() {
		return unitOfMeasureDao;
	}

	public void setUnitOfMeasureDao(UnitOfMeasureDAO unitOfMeasureDao) {
		this.unitOfMeasureDao = unitOfMeasureDao;
	}

	public List<UnitOfMeasure> getAll() {
        return unitOfMeasureDao.findAll();
    }

    public void persist(UnitOfMeasure cd) {
    	unitOfMeasureDao.persist(cd);
    }

    public UnitOfMeasure get(String id) {
        return unitOfMeasureDao.find(id);
    }

    public void update(UnitOfMeasure c) {
    	unitOfMeasureDao.merge(c);
    }

    public void delete(String id) {
    	unitOfMeasureDao.removeById(id);
    }

	public List<UnitOfMeasure> getByClass(String valueLike) {
		// TODO Auto-generated method stub
		return unitOfMeasureDao.getByClass(valueLike);
		
	}
}
