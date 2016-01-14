package it.geosolutions.opensdi2.persistence;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class RuleDaoImpl extends BaseDao<Rule, Long> implements RuleDao {

    @Override
    public void persist(Rule... entities) {
        super.persist(entities);
    }

    @Override
    public Rule merge(Rule entity) {
        return super.merge(entity);
    }

    @Override
    public boolean remove(Rule entity) {
        return super.remove(entity);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }

    private static String[] PKNames = {
            "id"
    };

    public String[] getPKNames() {
        return PKNames;
    }

    @Override
    public Class<Rule> getEntityType() {
        return Rule.class;
    }
}
