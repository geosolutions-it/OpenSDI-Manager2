/**
 *
 */
package it.geosolutions.opensdi.service;

import it.geosolutions.opensdi.model.AgrometDescriptor;
import it.geosolutions.opensdi.persistence.dao.AgrometDescriptorDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional(value = "opensdiTransactionManager")
public class AgrometDescriptorService {

    @Autowired
    private AgrometDescriptorDAO agrometDescriptorDao;

    public AgrometDescriptorDAO getAgrometDescriptorDao() {
		return agrometDescriptorDao;
	}

	public void setAgrometDescriptorDao(AgrometDescriptorDAO descriptorDao) {
		this.agrometDescriptorDao = descriptorDao;
	}

	public List<AgrometDescriptor> getAll() {
        return agrometDescriptorDao.findAll();
    }

    public void persist(AgrometDescriptor cd) {
        agrometDescriptorDao.persist(cd);
    }

    public AgrometDescriptor get(String id) {
        return agrometDescriptorDao.find(id);
    }

    public void update(AgrometDescriptor c) {
        agrometDescriptorDao.merge(c);
    }

    public void delete(String id) {
        agrometDescriptorDao.removeById(id);
    }
}
