package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltHerbaceousRelativeCoverService extends BaseService<AltHerbaceousRelativeCover, Integer> {

    @Autowired
    private AltHerbaceousRelativeCoverDao herbaceousRelativeCoverDao;

    @Override
    protected GenericVibiDao<AltHerbaceousRelativeCover, Integer> getDao() {
        return herbaceousRelativeCoverDao;
    }
}
