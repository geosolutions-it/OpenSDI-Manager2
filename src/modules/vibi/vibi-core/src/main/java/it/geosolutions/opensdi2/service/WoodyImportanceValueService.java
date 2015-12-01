package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousDao;
import it.geosolutions.opensdi2.persistence.WoodyImportanceValue;
import it.geosolutions.opensdi2.persistence.WoodyImportanceValueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class WoodyImportanceValueService {

    @Autowired
    private WoodyImportanceValueDao woodyImportanceValueDao;

    public List<WoodyImportanceValue> getAll() {
        return woodyImportanceValueDao.findAll();
    }
}
