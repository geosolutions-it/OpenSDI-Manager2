package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class BiomassRawDaoImpl extends BaseDao<BiomassRaw, String>
        implements BiomassRawDao {

    @Override
    public void persist(BiomassRaw... entities) {
        super.persist(entities);
    }

    @Override
    public BiomassRaw merge(BiomassRaw entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(BiomassRaw entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(String id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {};

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<BiomassRaw> getEntityType() {
        return BiomassRaw.class;
    }
}
