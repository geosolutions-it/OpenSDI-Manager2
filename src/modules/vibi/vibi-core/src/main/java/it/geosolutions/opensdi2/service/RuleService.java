package it.geosolutions.opensdi2.service;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Rule;
import it.geosolutions.opensdi2.persistence.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class RuleService extends BaseService<Rule, Long> {

    @Autowired
    private RuleDao ruleDao;

    @Override
    protected GenericVibiDao<Rule, Long> getDao() {
        return ruleDao;
    }

    public List<Rule> findRules(String service, String operation, String entity,
                                String format, String user, List<String> groups) {
        Search search = new Search(Rule.class);
        search.addFilterAnd(
                Filter.or(Filter.equal("service", service), Filter.equal("service", "*")),
                Filter.or(Filter.equal("operation", operation), Filter.equal("operation", "*")),
                Filter.or(Filter.equal("entity", entity), Filter.equal("entity", "*")),
                Filter.or(Filter.equal("format", format), Filter.equal("format", "*")),
                Filter.or(Filter.equal("user", user), Filter.equal("user", "*")),
                Filter.or(Filter.in("group", groups), Filter.equal("group", "*")));
        return ruleDao.search(search);
    }
}