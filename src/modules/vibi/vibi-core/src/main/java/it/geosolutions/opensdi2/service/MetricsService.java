package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class MetricsService extends BaseService<Metrics> {

    @Autowired
    private MetricsDao matricsDao;

    @Override
    protected GenericVibiDao getDao() {
        return matricsDao;
    }
}
