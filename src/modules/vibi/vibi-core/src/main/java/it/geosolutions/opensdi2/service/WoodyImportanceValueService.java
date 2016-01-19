package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class WoodyImportanceValueService extends BaseService<WoodyImportanceValue, Integer> {

    @Autowired
    private WoodyImportanceValueDao woodyImportanceValueDao;

    @Override
    protected GenericVibiDao<WoodyImportanceValue, Integer> getDao() {
        return woodyImportanceValueDao;
    }
}
