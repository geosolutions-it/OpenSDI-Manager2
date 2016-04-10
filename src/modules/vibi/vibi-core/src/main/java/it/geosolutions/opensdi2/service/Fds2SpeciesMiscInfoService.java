package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class Fds2SpeciesMiscInfoService extends BaseService<Fds2SpeciesMiscInfo, String> {

    @Autowired
    private Fds2SpeciesMiscInfoDao fds2SpeciesMiscInfoDao;

    @Override
    protected GenericVibiDao<Fds2SpeciesMiscInfo, String> getDao() {
        return fds2SpeciesMiscInfoDao;
    }
}
