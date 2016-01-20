package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.BiomassRaw;
import it.geosolutions.opensdi2.persistence.BiomassRawDao;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
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

    @Override
    public void persist(BiomassRaw entity) {
        entity.setFid(String.format("%d-%d-%d", entity.getPlotNo(),
                entity.getModuleId(), entity.getCorner()));
        super.persist(entity);
    }
}
