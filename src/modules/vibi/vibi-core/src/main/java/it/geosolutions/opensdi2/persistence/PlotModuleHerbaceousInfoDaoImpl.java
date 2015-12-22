package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousInfoDaoImpl extends BaseDao<PlotModuleHerbaceousInfo, String> implements PlotModuleHerbaceousInfoDao {

    @Override
    public void persist(PlotModuleHerbaceousInfo... entities) {
        super.persist(entities);
    }

    @Override
    public PlotModuleHerbaceousInfo merge(PlotModuleHerbaceousInfo entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(PlotModuleHerbaceousInfo entity) {
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
    public Class<PlotModuleHerbaceousInfo> getEntityType() {
        return PlotModuleHerbaceousInfo.class;
    }
}
