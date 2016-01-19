package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class CoverMidpointLookupDaoImpl extends BaseDao<CoverMidpointLookup, Integer>
        implements CoverMidpointLookupDao {

    @Override
    public void persist(CoverMidpointLookup... entities) {
        super.persist(entities);
    }

    @Override
    public CoverMidpointLookup merge(CoverMidpointLookup entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(CoverMidpointLookup entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(Integer id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {};

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<CoverMidpointLookup> getEntityType() {
        return CoverMidpointLookup.class;
    }
}
