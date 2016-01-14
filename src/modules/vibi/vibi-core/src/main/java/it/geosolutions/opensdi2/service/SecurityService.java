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
        if (size != null) {
            for (Rule rule : rules) {
                if (rule.getSize() == null || rule.getSize() < 0 || size <= rule.getSize()) {
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
