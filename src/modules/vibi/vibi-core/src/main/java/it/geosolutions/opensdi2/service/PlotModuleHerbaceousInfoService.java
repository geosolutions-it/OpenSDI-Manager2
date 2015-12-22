package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousInfoService extends BaseService<PlotModuleHerbaceousInfo>{

    @Autowired
    private PlotModuleHerbaceousInfoDao plotModuleHerbaceousInfoDao;


    @Override
    protected GenericVibiDao getDao() {
        return plotModuleHerbaceousInfoDao;
    }
}
