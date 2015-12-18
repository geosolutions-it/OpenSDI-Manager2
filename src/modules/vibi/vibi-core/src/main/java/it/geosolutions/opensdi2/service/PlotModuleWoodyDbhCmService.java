package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbhCm;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbhCmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyDbhCmService extends BaseService<PlotModuleWoodyDbhCm> {

    @Autowired
    private PlotModuleWoodyDbhCmDao plotModuleWoodyDbhCmDao;

    @Override
    protected GenericVibiDao getDao() {
        return plotModuleWoodyDbhCmDao;
    }
}
