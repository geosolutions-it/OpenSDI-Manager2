package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousDaoImpl extends BaseDao<PlotModuleHerbaceous, String> implements PlotModuleHerbaceousDao {

    @Override
    public void persist(PlotModuleHerbaceous... entities) {
        super.persist(entities);
    }

    @Override
    public PlotModuleHerbaceous merge(PlotModuleHerbaceous entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(PlotModuleHerbaceous entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(String id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {
            "fid"
    };

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    protected Class<PlotModuleHerbaceous> getEntityType() {
        return PlotModuleHerbaceous.class;
    }
}
