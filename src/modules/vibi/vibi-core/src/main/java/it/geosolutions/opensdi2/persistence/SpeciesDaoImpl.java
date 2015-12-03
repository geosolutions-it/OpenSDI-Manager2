package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class SpeciesDaoImpl extends BaseDao<Species, String> implements SpeciesDao {

    @Override
    public void persist(Species... entities) {
        super.persist(entities);
    }

    @Override
    public Species merge(Species entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Species entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(String id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {
            "plot_no"
    };

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<Species> getEntityType() {
        return Species.class;
    }
}
