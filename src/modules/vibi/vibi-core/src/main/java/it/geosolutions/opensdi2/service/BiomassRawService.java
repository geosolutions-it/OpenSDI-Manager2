package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
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
}
