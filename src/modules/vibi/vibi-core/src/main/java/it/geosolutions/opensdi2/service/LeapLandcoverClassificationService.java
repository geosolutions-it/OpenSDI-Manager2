package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.LeapLandcoverClassification;
import it.geosolutions.opensdi2.persistence.LeapLandcoverClassificationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class LeapLandcoverClassificationService extends BaseService<LeapLandcoverClassification, String> {

    @Autowired
    private LeapLandcoverClassificationDao leapLandcoverClassificationDao;

    @Override
    protected GenericVibiDao<LeapLandcoverClassification, String> getDao() {
        return leapLandcoverClassificationDao;
    }
}
