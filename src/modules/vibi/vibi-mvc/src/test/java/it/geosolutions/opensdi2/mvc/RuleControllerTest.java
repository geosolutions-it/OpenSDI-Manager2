/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        MvcTestsUtils.create("rule", MvcTestsUtils.readResourceFile("rule/create_user_rule.json"));
        List<Rule> rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'test_user'", null, null, null, null);
        assertThat(rules.size(), is(1));
        Long idRule = rules.get(0).getId();
        validateRule(rules.get(0), "test_user", "*", "crud", "delete", "*", "*", -1L);
        MvcTestsUtils.update("rule", idRule.toString(), MvcTestsUtils.readResourceFile("rule/update_user_rule.json"));
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'test_user'", null, null, null, null);
        assertThat(rules.size(), is(1));
        validateRule(rules.get(0), "test_user", "*", "crud", "delete", "species", "*", -1L);
        MvcTestsUtils.delete("rule", idRule.toString());
        rules = MvcTestsUtils.list(RULE_GENERIC_TYPE, "rule", null, "user:=:'test_user'", null, null, null, null);
        assertThat(rules.size(), is(0));
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
