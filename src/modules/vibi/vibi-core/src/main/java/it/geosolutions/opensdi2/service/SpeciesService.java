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
        persistDerivated(entity.getAuthority(), new Authority());
        persistDerivated(entity.getFamily(), new Family());
        persistDerivated(entity.getInd(), new Ind());
        persistDerivated(entity.getForm(), new Form());
        persistDerivated(entity.getHabit(), new Habit());
        persistDerivated(entity.getGroupp(), new Groupp());
        persistDerivated(entity.getShade(), new Shade());
        persistDerivated(entity.getNativity(), new Nativity());
        persistDerivated(entity.getCode1(), new Code1());
        persistDerivated(entity.getCode2(), new Code2());
        persistDerivated(entity.getCode3(), new Code3());
        persistDerivated(entity.getCode4(), new Code4());
        persistDerivated(entity.getCode5(), new Code5());
    }
}
