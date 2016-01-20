package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousInfo;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousInfoService extends BaseService<PlotModuleHerbaceousInfo, String> {

    @Autowired
    private PlotModuleHerbaceousInfoDao plotModuleHerbaceousInfoDao;


    @Override
    protected GenericVibiDao<PlotModuleHerbaceousInfo, String> getDao() {
        return plotModuleHerbaceousInfoDao;
    }

    @Override
    public void persist(PlotModuleHerbaceousInfo entity) {
        entity.setFid(String.format("%d-%d-%d-%s", entity.getPlotNo(), entity.getModuleId(),
                entity.getCornerId(), entity.getInfo().toLowerCase()));
        super.persist(entity);
    }
}
