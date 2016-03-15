package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import it.geosolutions.opensdi2.persistence.derivated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousService extends BaseService<PlotModuleHerbaceous, String>{

    @Autowired
    private PlotModuleHerbaceousDao plotModuleHerbaceousDao;


    @Override
    protected GenericVibiDao<PlotModuleHerbaceous, String> getDao() {
        return plotModuleHerbaceousDao;
    }

    @Override
    public void persist(PlotModuleHerbaceous entity) {
        entity.setFid(String.format("%s-%d-%d-%s", entity.getPlotNo(), entity.getModuleId(),
                entity.getCornerId(), entity.getSpecies().toLowerCase()));
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, PlotModuleHerbaceous entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(PlotModuleHerbaceous entity) {
        persistDerivated(entity.getModuleId(), new Module());
        persistDerivated(entity.getCornerId(), new Corner());
        persistDerivated(entity.getDepth(), new Depth());
    }
}
