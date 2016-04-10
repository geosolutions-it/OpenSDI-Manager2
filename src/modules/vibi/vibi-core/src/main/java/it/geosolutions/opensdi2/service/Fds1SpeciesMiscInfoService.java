package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.Fds1SpeciesMiscInfo;
import it.geosolutions.opensdi2.persistence.Fds1SpeciesMiscInfoDao;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class Fds1SpeciesMiscInfoService extends BaseService<Fds1SpeciesMiscInfo, String> {

    @Autowired
    private Fds1SpeciesMiscInfoDao fds1SpeciesMiscInfoDao;

    @Override
    protected GenericVibiDao<Fds1SpeciesMiscInfo, String> getDao() {
        return fds1SpeciesMiscInfoDao;
    }
}
