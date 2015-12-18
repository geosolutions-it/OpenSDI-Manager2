package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyDbhDaoImpl extends BaseDao<PlotModuleWoodyDbh, String> implements PlotModuleWoodyDbhDao {

    @Override
    public void persist(PlotModuleWoodyDbh... entities) {
        super.persist(entities);
    }

    @Override
    public PlotModuleWoodyDbh merge(PlotModuleWoodyDbh entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(PlotModuleWoodyDbh entity) {
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
    public Class<PlotModuleWoodyDbh> getEntityType() {
        return PlotModuleWoodyDbh.class;
    }
}
