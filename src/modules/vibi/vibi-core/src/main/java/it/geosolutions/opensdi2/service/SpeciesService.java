package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Species;
import it.geosolutions.opensdi2.persistence.SpeciesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class SpeciesService extends BaseService<Species> {

    @Autowired
    private SpeciesDao speciesDao;

    @Override
    protected GenericVibiDao getDao() {
        return speciesDao;
    }
}
