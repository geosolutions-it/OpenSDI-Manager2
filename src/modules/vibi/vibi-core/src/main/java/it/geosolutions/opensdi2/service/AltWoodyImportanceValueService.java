package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltWoodyImportanceValueService extends BaseService<AltWoodyImportanceValue, Integer> {

    @Autowired
    private AltWoodyImportanceValueDao woodyImportanceValueDao;

    @Override
    protected GenericVibiDao<AltWoodyImportanceValue, Integer> getDao() {
        return woodyImportanceValueDao;
    }
}
