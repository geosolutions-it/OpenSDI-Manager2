package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Species;
import it.geosolutions.opensdi2.persistence.SpeciesDao;
import it.geosolutions.opensdi2.persistence.derivated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class SpeciesService extends BaseService<Species, String> {

    @Autowired
    private SpeciesDao speciesDao;

    @Override
    protected GenericVibiDao<Species, String> getDao() {
        return speciesDao;
    }

    @Override
    public void persist(Species entity) {
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, Species entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(Species entity) {
        persistDerivated(entity.getForm(), new Form());
        persistDerivated(entity.getHabit(), new Habit());
        persistDerivated(entity.getShade(), new Shade());
        persistDerivated(entity.getOhStatus(), new OhStatus());
        persistDerivated(entity.getEmp(), new Ind());
        persistDerivated(entity.getMw(), new Ind());
        persistDerivated(entity.getNcne(), new Ind());
    }
}
