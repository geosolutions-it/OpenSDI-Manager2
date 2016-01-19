package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class ClassCodeModNatureServeService extends BaseService<ClassCodeModNatureServe, String> {

    @Autowired
    private ClassCodeModNatureServeDao classCodeModNatureServeDao;

    @Override
    protected GenericVibiDao<ClassCodeModNatureServe, String> getDao() {
        return classCodeModNatureServeDao;
    }
}
