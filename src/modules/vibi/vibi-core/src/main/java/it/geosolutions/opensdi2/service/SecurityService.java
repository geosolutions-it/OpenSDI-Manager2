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
package it.geosolutions.opensdi2.service;

import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.core.model.UserGroup;
import it.geosolutions.opensdi2.persistence.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecurityService {

    @Autowired
    RuleService rulesService;

    public void validate(String service, String operation, String entity, String format, Integer size) {
        UserInfo userInfo = new UserInfo(SecurityContextHolder.getContext().getAuthentication());
        List<Rule> rules = rulesService.findRules(service, operation, entity, format, userInfo.name, userInfo.groups);
        if (rules.isEmpty()) {
            throw new SecurityException();
        }
        Collections.sort(rules, new Comparator<Rule>() {
            @Override
            public int compare(Rule rule1, Rule rule2) {
                if (rule1.getPriority().equals(rule2.getPriority())) {
                    return 0;
                }
                if (rule1.getPriority() < rule2.getPriority()) {
                    return -1;
                }
                return 1;
            }
        });
        if (size != null) {
            for (Rule rule : rules) {
                if (!rule.getAllow() && (rule.getSize() == null || rule.getSize() < 0 || size > rule.getSize())) {
                    throw new SecurityException();
                }
                if (rule.getAllow() || rule.getSize() == null || rule.getSize() < 0 || size <= rule.getSize()) {
                    return;
                }
            }
            throw new SecurityException();
        }
    }

    private static class UserInfo {

        String name;
        List<String> groups = new ArrayList<String>();

        public UserInfo(Authentication authentication) {
            if (authentication == null) {
                return;
            }
            handlePrincipal(authentication.getPrincipal());
        }

        private void handlePrincipal(Object principal) {
            if (principal == null) {
                return;
            }
            if (principal instanceof User) {
                handleUser((User) principal);
            } else if (principal instanceof String) {
                name = (String) principal;
            }
        }

        private void handleUser(User user) {
            name = user.getName();
            if (user.getGroups() == null) {
                return;
            }
            for (UserGroup group : user.getGroups()) {
                groups.add(group.getGroupName());
            }
        }
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "")
    public class SecurityException extends RuntimeException {
    }
}
