package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class Fds2SpeciesMiscInfoDaoImpl extends BaseDao<Fds2SpeciesMiscInfo, String>
        implements Fds2SpeciesMiscInfoDao {

    @Override
    public void persist(Fds2SpeciesMiscInfo... entities) {
        super.persist(entities);
    }

    @Override
    public Fds2SpeciesMiscInfo merge(Fds2SpeciesMiscInfo entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Fds2SpeciesMiscInfo entity) {
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
    public Class<Fds2SpeciesMiscInfo> getEntityType() {
        return Fds2SpeciesMiscInfo.class;
    }
}
