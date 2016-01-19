package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.persistence.PlotDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotService extends BaseService<Plot, Long> {

    @Autowired
    private PlotDao plotDao;

    @Override
    protected GenericVibiDao<Plot, Long> getDao() {
        return plotDao;
    }
}
