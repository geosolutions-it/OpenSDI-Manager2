package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class HerbaceousRelativeCoverDaoImpl extends BaseDao<HerbaceousRelativeCover, Integer>
        implements HerbaceousRelativeCoverDao {

    @Override
    public void persist(HerbaceousRelativeCover... entities) {
        super.persist(entities);
    }

    @Override
    public HerbaceousRelativeCover merge(HerbaceousRelativeCover entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(HerbaceousRelativeCover entity) {
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
    public Class<HerbaceousRelativeCover> getEntityType() {
        return HerbaceousRelativeCover.class;
    }
}
