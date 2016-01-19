package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCoverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class HerbaceousRelativeCoverService extends BaseService<HerbaceousRelativeCover, Integer> {

    @Autowired
    private HerbaceousRelativeCoverDao herbaceousRelativeCoverDao;

    @Override
    protected GenericVibiDao<HerbaceousRelativeCover, Integer> getDao() {
        return herbaceousRelativeCoverDao;
    }
}
