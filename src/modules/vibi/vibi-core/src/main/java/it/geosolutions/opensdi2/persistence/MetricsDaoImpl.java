package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class MetricsDaoImpl extends BaseDao<Metrics, Long> implements MetricsDao {

    @Override
    public void persist(Metrics... entities) {
        super.persist(entities);
    }

    @Override
    public Metrics merge(Metrics entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Metrics entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {};

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<Metrics> getEntityType() {
        return Metrics.class;
    }
}
