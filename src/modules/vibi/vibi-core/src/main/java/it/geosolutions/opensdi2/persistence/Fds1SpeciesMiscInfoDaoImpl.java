package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class Fds1SpeciesMiscInfoDaoImpl extends BaseDao<Fds1SpeciesMiscInfo, String>
        implements Fds1SpeciesMiscInfoDao {

    @Override
    public void persist(Fds1SpeciesMiscInfo... entities) {
        super.persist(entities);
    }

    @Override
    public Fds1SpeciesMiscInfo merge(Fds1SpeciesMiscInfo entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Fds1SpeciesMiscInfo entity) {
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
    public Class<Fds1SpeciesMiscInfo> getEntityType() {
        return Fds1SpeciesMiscInfo.class;
    }
}
