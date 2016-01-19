package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyDbhCmDaoImpl extends BaseDao<PlotModuleWoodyDbhCm, Integer> implements PlotModuleWoodyDbhCmDao {

    @Override
    public void persist(PlotModuleWoodyDbhCm... entities) {
        super.persist(entities);
    }

    @Override
    public PlotModuleWoodyDbhCm merge(PlotModuleWoodyDbhCm entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(PlotModuleWoodyDbhCm entity) {
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
    public Class<PlotModuleWoodyDbhCm> getEntityType() {
        return PlotModuleWoodyDbhCm.class;
    }
}
