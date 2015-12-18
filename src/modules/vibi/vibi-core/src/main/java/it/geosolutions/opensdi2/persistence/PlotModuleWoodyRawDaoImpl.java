package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleWoodyRawDaoImpl extends BaseDao<PlotModuleWoodyRaw, String> implements PlotModuleWoodyRawDao {

    @Override
    public void persist(PlotModuleWoodyRaw... entities) {
        super.persist(entities);
    }

    @Override
    public PlotModuleWoodyRaw merge(PlotModuleWoodyRaw entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(PlotModuleWoodyRaw entity) {
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
    public Class<PlotModuleWoodyRaw> getEntityType() {
        return PlotModuleWoodyRaw.class;
    }
}
