package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class WoodyImportanceValueDaoImpl extends BaseDao<WoodyImportanceValue, String> implements WoodyImportanceValueDao {

    @Override
    public void persist(WoodyImportanceValue... entities) {
        super.persist(entities);
    }

    @Override
    public WoodyImportanceValue merge(WoodyImportanceValue entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(WoodyImportanceValue entity) {
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
    protected Class<WoodyImportanceValue> getEntityType() {
        return WoodyImportanceValue.class;
    }
}
