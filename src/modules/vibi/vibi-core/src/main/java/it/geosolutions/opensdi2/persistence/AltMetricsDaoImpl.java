package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltMetricsDaoImpl extends BaseDao<AltMetrics, Integer> implements AltMetricsDao {

    @Override
    public void persist(AltMetrics... entities) {
        super.persist(entities);
    }

    @Override
    public AltMetrics merge(AltMetrics entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(AltMetrics entity) {
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
    public Class<AltMetrics> getEntityType() {
        return AltMetrics.class;
    }
}
