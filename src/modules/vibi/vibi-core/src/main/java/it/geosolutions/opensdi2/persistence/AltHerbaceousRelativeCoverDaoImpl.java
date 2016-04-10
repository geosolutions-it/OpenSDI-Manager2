package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltHerbaceousRelativeCoverDaoImpl extends BaseDao<AltHerbaceousRelativeCover, Integer>
        implements AltHerbaceousRelativeCoverDao {

    @Override
    public void persist(AltHerbaceousRelativeCover... entities) {
        super.persist(entities);
    }

    @Override
    public AltHerbaceousRelativeCover merge(AltHerbaceousRelativeCover entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(AltHerbaceousRelativeCover entity) {
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
    public Class<AltHerbaceousRelativeCover> getEntityType() {
        return AltHerbaceousRelativeCover.class;
    }
}
