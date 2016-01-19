package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class ClassCodeModNatureServeDaoImpl extends BaseDao<ClassCodeModNatureServe, String>
        implements ClassCodeModNatureServeDao {

    @Override
    public void persist(ClassCodeModNatureServe... entities) {
        super.persist(entities);
    }

    @Override
    public ClassCodeModNatureServe merge(ClassCodeModNatureServe entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(ClassCodeModNatureServe entity) {
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
    public Class<ClassCodeModNatureServe> getEntityType() {
        return ClassCodeModNatureServe.class;
    }
}
