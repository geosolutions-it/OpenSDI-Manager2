package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbh;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbhDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyDbhService extends BaseService<PlotModuleWoodyDbh> {

    @Autowired
    private PlotModuleWoodyDbhDao plotModuleWoodyDbhDao;

    @Override
    protected GenericVibiDao getDao() {
        return plotModuleWoodyDbhDao;
    }
}
