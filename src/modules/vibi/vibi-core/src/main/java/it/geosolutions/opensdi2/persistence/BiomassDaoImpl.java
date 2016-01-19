package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class BiomassDaoImpl extends BaseDao<Biomass, String>
        implements BiomassDao {

    @Override
    public void persist(Biomass... entities) {
        super.persist(entities);
    }

    @Override
    public Biomass merge(Biomass entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Biomass entity) {
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
    public Class<Biomass> getEntityType() {
        return Biomass.class;
    }
}
