package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.persistence.PlotDao;
import it.geosolutions.opensdi2.persistence.derivated.*;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional(value = "opensdiTransactionManager")
public class PlotService extends BaseService<Plot, String> {

    @Autowired
    private PlotDao plotDao;

    @Override
    protected GenericVibiDao<Plot, String> getDao() {
        return plotDao;
    }

    @Override
    public void persist(Plot entity) {
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, Plot entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(Plot entity) {
        persistDerivated(entity.getVegclass(), new VegClass());
        persistDerivated(entity.getHgmclass(), new HgmClass());
        persistDerivated(entity.getHgmsubclass(), new HgmSubClass());
        persistDerivated(entity.getHgmgroup(), new HgmGroup());
        persistDerivated(entity.getLandformType(), new LandFormType());
        persistDerivated(entity.getHomogeneity(), new Homogeneity());
        persistDerivated(entity.getStandSize(), new StandSize());
        persistDerivated(entity.getDrainage(), new Drainage());
        persistDerivated(entity.getSalinity(), new Salinity());
        persistDerivated(entity.getHydrologicRegime(), new HydrologicRegime());
        persistDerivated(entity.getOneoDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getOneoDisturbanceSeverity(), new DisturbanceSeverity());
        persistDerivated(entity.getTwooDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getTwooDisturbanceSeverity(), new DisturbanceSeverity());
        persistDerivated(entity.getThreeoDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getThreeoDisturbanceSeverity(), new DisturbanceSeverity());
    }
}
