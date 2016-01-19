package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class BiomassService extends BaseService<Biomass, String> {

    @Autowired
    private BiomassDao biomassDao;

    @Override
    protected GenericVibiDao<Biomass, String> getDao() {
        return biomassDao;
    }
}
