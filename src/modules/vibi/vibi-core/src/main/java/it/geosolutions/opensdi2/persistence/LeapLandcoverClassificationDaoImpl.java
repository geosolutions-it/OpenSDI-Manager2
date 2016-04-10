package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class LeapLandcoverClassificationDaoImpl extends BaseDao<LeapLandcoverClassification, String>
        implements LeapLandcoverClassificationDao {

    @Override
    public void persist(LeapLandcoverClassification... entities) {
        super.persist(entities);
    }

    @Override
    public LeapLandcoverClassification merge(LeapLandcoverClassification entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(LeapLandcoverClassification entity) {
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
    public Class<LeapLandcoverClassification> getEntityType() {
        return LeapLandcoverClassification.class;
    }
}
