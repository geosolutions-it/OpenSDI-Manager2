package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class CoverMidpointLookupService extends BaseService<CoverMidpointLookup, Integer> {

    @Autowired
    private CoverMidpointLookupDao coverMidpointLookupDao;

    @Override
    protected GenericVibiDao<CoverMidpointLookup, Integer> getDao() {
        return coverMidpointLookupDao;
    }
}
