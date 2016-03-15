package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.BiomassRaw;
import it.geosolutions.opensdi2.persistence.BiomassRawDao;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyRaw;
import it.geosolutions.opensdi2.persistence.derivated.BiomassAccuracy;
import it.geosolutions.opensdi2.persistence.derivated.Corner;
import it.geosolutions.opensdi2.persistence.derivated.DbhClass;
import it.geosolutions.opensdi2.persistence.derivated.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class BiomassRawService extends BaseService<BiomassRaw, String> {

    @Autowired
    private BiomassRawDao biomassRawDao;

    @Override
    protected GenericVibiDao<BiomassRaw, String> getDao() {
        return biomassRawDao;
    }

    @Override
    public void persist(BiomassRaw entity) {
        entity.setFid(String.format("%s-%d-%d", entity.getPlotNo(),
                entity.getModuleId(), entity.getCorner()));
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, BiomassRaw entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(BiomassRaw entity) {
        persistDerivated(entity.getModuleId(), new Module());
        persistDerivated(entity.getCorner(), new Corner());
        persistDerivated(entity.getActualOrDerived(), new BiomassAccuracy());
    }
}
