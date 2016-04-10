package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class AltWoodyImportanceValueDaoImpl extends BaseDao<AltWoodyImportanceValue, Integer> implements AltWoodyImportanceValueDao {

    @Override
    public void persist(AltWoodyImportanceValue... entities) {
        super.persist(entities);
    }

    @Override
    public AltWoodyImportanceValue merge(AltWoodyImportanceValue entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(AltWoodyImportanceValue entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(Integer id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {
            "fid"
    };

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<AltWoodyImportanceValue> getEntityType() {
        return AltWoodyImportanceValue.class;
    }
}
