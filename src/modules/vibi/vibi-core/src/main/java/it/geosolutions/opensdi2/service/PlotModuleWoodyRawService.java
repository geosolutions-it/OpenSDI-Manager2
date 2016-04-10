package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import it.geosolutions.opensdi2.persistence.derivated.Corner;
import it.geosolutions.opensdi2.persistence.derivated.DbhClass;
import it.geosolutions.opensdi2.persistence.derivated.Depth;
import it.geosolutions.opensdi2.persistence.derivated.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        entity.setFid(UUID.randomUUID().toString());
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, PlotModuleWoodyRaw entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(PlotModuleWoodyRaw entity) {
        persistDerivated(entity.getModuleId(), new Module());
        persistDerivated(entity.getDbhClass(), new DbhClass());
    }
}
