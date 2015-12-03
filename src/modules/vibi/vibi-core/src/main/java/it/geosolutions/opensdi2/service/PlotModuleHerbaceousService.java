package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCoverDao;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousService extends BaseService<PlotModuleHerbaceous>{

    @Autowired
    private PlotModuleHerbaceousDao plotModuleHerbaceousDao;


    @Override
    protected GenericVibiDao getDao() {
        return plotModuleHerbaceousDao;
    }
}
