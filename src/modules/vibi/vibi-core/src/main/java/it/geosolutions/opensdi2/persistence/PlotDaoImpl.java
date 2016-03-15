package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotDaoImpl extends BaseDao<Plot, String> implements PlotDao {

    @Override
    public void persist(Plot... entities) {
        super.persist(entities);
    }

    @Override
    public Plot merge(Plot entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Plot entity) {
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
    public Class<Plot> getEntityType() {
        return Plot.class;
    }
}
