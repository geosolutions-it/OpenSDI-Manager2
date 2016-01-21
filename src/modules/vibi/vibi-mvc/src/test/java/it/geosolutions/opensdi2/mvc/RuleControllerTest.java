package it.geosolutions.opensdi2.mvc;

import com.sun.jersey.api.client.GenericType;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class RuleControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<Rule>> RULE_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<Rule>>() {
    };

    @Test
    public void testListAdminRule() {
        List<Rule> rules = MvcTestsUtils.list(RULE_GENERIC_TYPE,
                "rule", null, "id:=:'0';size:<=:'0';user:=:'admin'", "user:ASC;service:ASC;size:DESC", null, null, null);
        assertThat(rules.size(), is(1));
        Rule rule = rules.get(0);
        assertThat(rule.getId(), is(0L));
        validateRule(rule, MvcTestsUtils.ADMIN_USER, "*", "*", "*", "*", "*", -1L);
    }

    @Test
    public void testCrudRule() {
        MvcTestsUtils.create("rule", MvcTestsUtils.readResourceFile("rule/create_group_everyone.json"));
        MvcTestsUtils.create("rule", MvcTestsUtils.readResourceFile("rule/create_user_rule_1.json"));
        MvcTestsUtils.create("rule", MvcTestsUtils.readResourceFile("rule/create_user_rule_2.json"));
        List<Rule> rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, null, null, null, null, null);
        assertThat(rules.size(), is(4));
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'admin'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), MvcTestsUtils.ADMIN_USER, "*", "*", "*", "*", "*", -1L);
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'user';service:=:'crud'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), "user", "*", "crud", "delete", "*", "*", -1L);
        Long idRule1 = rules.get(0).getId();
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'user';service:=:'download'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), "user", "*", "download", "export", "*", "csv", 1000L);
        Long idRule2 = rules.get(0).getId();
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "group:=:'everyone'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), "*", "everyone", "crud", "read", "*", "*", -1L);
        Long idRule3 = rules.get(0).getId();
        MvcTestsUtils.update("rule", idRule3.toString(), MvcTestsUtils.readResourceFile("rule/update_group_everyone.json"));
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "group:=:'everyone'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), "*", "everyone", "crud", "read", "*", "*", 100L);
        MvcTestsUtils.delete("rule", idRule1.toString());
        MvcTestsUtils.delete("rule", idRule2.toString());
        MvcTestsUtils.delete("rule", idRule3.toString());
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, null, null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), MvcTestsUtils.ADMIN_USER, "*", "*", "*", "*", "*", -1L);
    }

    private void validateRule(Rule rule, String user, String group, String service,
                              String operation, String entity, String format, Long size) {
        assertThat(rule.getUser(), is(user));
        assertThat(rule.getGroup(), is(group));
        assertThat(rule.getService(), is(service));
        assertThat(rule.getOperation(), is(operation));
        assertThat(rule.getEntity(), is(entity));
        assertThat(rule.getFormat(), is(format));
        assertThat(rule.getSize(), is(size));
    }
}
