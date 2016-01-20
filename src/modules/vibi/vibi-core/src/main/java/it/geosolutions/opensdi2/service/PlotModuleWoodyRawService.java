package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyRawService extends BaseService<PlotModuleWoodyRaw, String> {

    @Autowired
    private PlotModuleWoodyRawDao plotModuleWoodyRawDao;

    @Override
    protected GenericVibiDao<PlotModuleWoodyRaw, String> getDao() {
        return plotModuleWoodyRawDao;
    }

    @Override
    public void persist(PlotModuleWoodyRaw entity) {
        entity.setFid(String.format("%s-%s-%s-%s", entity.getPlotNo(),
                entity.getModuleId(), entity.getSpecies(), entity.getDbhClass()));
        super.persist(entity);
    }
}
