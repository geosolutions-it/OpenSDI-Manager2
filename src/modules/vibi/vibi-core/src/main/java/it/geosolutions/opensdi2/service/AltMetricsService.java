package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltMetricsService extends BaseService<AltMetrics, Integer> {

    @Autowired
    private AltMetricsDao matricsDao;

    @Override
    protected GenericVibiDao<AltMetrics, Integer> getDao() {
        return matricsDao;
    }
}
