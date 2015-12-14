/*
 *  Copyright (C) 2007 - 2014 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AbstractFilter;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.util.StringUtils;

import it.geosolutions.opensdi2.service.UserGroupService;

/**
 * User group service LDAP implementation
 * 
 * @author adiaz
 */
public class LDAPUserGroupServiceImpl implements UserGroupService {

    private final static Logger LOGGER = Logger.getLogger(LDAPUserGroupServiceImpl.class);

    /**
     * LDAP template
     */
    LdapTemplate ldapTemplate;

    /**
     * Base DN to search
     */
    private String LDAP_USER_BASE_SEARCH;

    /**
     * Force properties key, value properties in the search
     */
    private String LDAP_FORCE_BASE_SEARCH;

    /**
     * Properties to perform the search
     */
    private String LDAP_PROPERTIES_SEARCH;

    /**
     * CN property
     */
    private String LDAP_CN_PROPERTY;

    /**
     * UID property
     */
    private String LDAP_UID_PROPERTY;

    private static final String LDAP_PROPERTIES_SEARCH_SEPARATOR = ",";

    private static final String LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR = "=";

    /**
     * Attribute to add to each group with the user uid
     */
    private String memberAttribute = "memberUid";

    /**
     * Parameter name for the groups
     */
    private static final String parameterGroup = "objectClass";

    /**
     * Parameter group value for {@link #parameterGroup}
     */
    private static final String parameterGroupValue = "posixGroup";

    /**
     * Search on LDAP
     * 
     * @param uid
     * 
     * @return Map with the user attributes
     */
    @Override
    public List<Map<String, Object>> search(String uid) {
        try {

            List<Attributes> list = searchInLdap(uid);

            List<Map<String, Object>> entries = new LinkedList<Map<String, Object>>();
            for (Attributes attrs : list) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put(LDAP_CN_PROPERTY, attrs.get(LDAP_CN_PROPERTY).get());
                values.put(LDAP_UID_PROPERTY, attrs.get(LDAP_UID_PROPERTY).get());
                entries.add(values);
                if (LOGGER.isTraceEnabled())
                    LOGGER.trace(
                            " Person Common Name = " + (String) attrs.get(LDAP_CN_PROPERTY).get());
            }

            return entries;
        } catch (Exception e) {
            LOGGER.error("Error getting ldap users", e);
        }
        return null;
    }

    /**
     * Save user groups for an user
     * 
     * @param uid
     * @param groups
     * 
     * @return flag that indicates if the operation could be performed
     */
    @Override
    public boolean setUserGroups(String uid, List<String> groups) {
        boolean success = false;
        try {
            List<Attributes> userAttr = searchInLdap(uid);
            if (userAttr != null && userAttr.size() == 1) {

                // Get complete user unit and current user groups
                List<String> actualGroups = getUserGroups(uid);

                // change user groups
                success = true;
                if (groups != null) {
                    // remove not included groups
                    for (String group : actualGroups) {
                        if (!groups.contains(group)) {
                            success = success && removeUserToGroup(uid, group);
                        } else {
                            groups.remove(group);
                        }
                    }
                    // add new groups
                    for (String group : groups) {
                        success = success && addUserToGroup(uid,
                                searchDnInLdap(group).get(0).name.toString());
                    }
                } else if (!actualGroups.isEmpty()) {
                    // remove all groups
                    for (String group : actualGroups) {
                        success = success && removeUserToGroup(uid, group);
                    }
                }

            } else if (userAttr == null) {
                LOGGER.error("User not found " + uid);
            } else {
                LOGGER.error("Incongruent users found: " + userAttr.size());
            }
        } catch (Exception e) {
            LOGGER.error("Error updating user groups", e);
        }
        return success;
    }

    /**
     * Get groups for an user
     * 
     * @param uid
     * 
     * @return groups that the user is member of
     */
    @Override
    public List<String> getUserGroups(String uid) {
        List<String> actualGroups = new LinkedList<String>();
        List<LDAPUnit> ldapGroups = searchInLdap(
                new EqualsFilter(parameterGroup, parameterGroupValue));
        for (LDAPUnit ldapGroup : ldapGroups) {
            if (ldapGroup.members != null) {
                for (Object user : ldapGroup.members) {
                    String userGroupDn = StringUtils.trimAllWhitespace(user.toString());
                    String userDn = StringUtils.trimAllWhitespace(uid);
                    if (userGroupDn.equals(userDn)) {
                        actualGroups.add(ldapGroup.name.toString());
                        break;
                    }
                }
            }
        }
        return actualGroups;
    }

    /**
     * Get attributes of an user
     * 
     * @param search
     * 
     * @return list of attributes of an user
     */
    private List<Attributes> searchInLdap(String search) {
        try {

            AndFilter filter = new AndFilter();
            OrFilter orFilter = new OrFilter();
            for (String property : LDAP_PROPERTIES_SEARCH.split(LDAP_PROPERTIES_SEARCH_SEPARATOR)) {
                orFilter.or(new LikeFilter(property, search));
            }
            if (LDAP_FORCE_BASE_SEARCH != null && !LDAP_FORCE_BASE_SEARCH.isEmpty()) {
                for (String property : LDAP_FORCE_BASE_SEARCH
                        .split(LDAP_PROPERTIES_SEARCH_SEPARATOR)) {
                    filter.and(new EqualsFilter(
                            property.split(LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR)[0],
                            property.split(LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR)[1]));
                }
            }
            filter.and(orFilter);

            /**
             * Example: OU=people,DC=nurc,DC=nato
             * 
             * cn=GLOBAL_WRITTER memberUid=admin
             */
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(
                        "************************************ Searching ***************************************\n"
                                + filter.encode());

            @SuppressWarnings("unchecked")
            List<Attributes> list = ldapTemplate.search(LDAP_USER_BASE_SEARCH, filter.encode(),
                    new AttributesMapper() {
                        @Override
                        public Object mapFromAttributes(Attributes attrs) throws NamingException {
                            return attrs;
                        }
                    });

            return list;
        } catch (Exception e) {
            LOGGER.error("Error getting ldap users", e);
        }
        return null;
    }

    /**
     * Search a list of LDAP units
     * 
     * @param search
     * @return
     */
    private List<LDAPUnit> searchDnInLdap(String search) {
        try {

            AndFilter filter = new AndFilter();
            OrFilter orFilter = new OrFilter();
            for (String property : LDAP_PROPERTIES_SEARCH.split(LDAP_PROPERTIES_SEARCH_SEPARATOR)) {
                orFilter.or(new LikeFilter(property, search));
            }
            if (LDAP_FORCE_BASE_SEARCH != null && !LDAP_FORCE_BASE_SEARCH.isEmpty()) {
                for (String property : LDAP_FORCE_BASE_SEARCH
                        .split(LDAP_PROPERTIES_SEARCH_SEPARATOR)) {
                    filter.and(new EqualsFilter(
                            property.split(LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR)[0],
                            property.split(LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR)[1]));
                }
            }
            filter.and(orFilter);

            return searchInLdap(filter);
        } catch (Exception e) {
            LOGGER.error("Error getting dn on ldap", e);
        }
        return null;
    }

    /**
     * Search LDAP units by filter
     * 
     * @param filter
     * @return
     */
    private List<LDAPUnit> searchInLdap(AbstractFilter filter) {
        try {

            /**
             * Example: OU=people,DC=nurc,DC=nato
             * 
             * cn=GLOBAL_WRITTER memberUid=admin
             */
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(
                        "************************************ Searching ***************************************\n"
                                + filter.encode());

            @SuppressWarnings("unchecked")
            List<LDAPUnit> names = ldapTemplate.search(LDAP_USER_BASE_SEARCH, filter.encode(),
                    new ContextMapper() {
                        @Override
                        public Object mapFromContext(Object ctx) {
                            DirContextAdapter context = (DirContextAdapter) ctx;
                            LDAPUnit unit = new LDAPUnit();
                            unit.name = context.getDn();
                            unit.members = context.getStringAttributes(memberAttribute);
                            return unit;
                        }
                    });

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Found " + names);

            return names;
        } catch (Exception e) {
            LOGGER.error("Error getting dn on ldap", e);
        }
        return null;
    }

    /**
     * Add an user in a group by DN
     * 
     * @param userDinstinguishedName
     * @param groupDistinguishedName
     * 
     * @return true if we can change or false otherwise
     */
    private boolean addUserToGroup(String uid, String groupDistinguishedName) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Group name: " + groupDistinguishedName);

            String distinguishedGroupName = groupDistinguishedName;

            ModificationItem[] modItems = new ModificationItem[] { new ModificationItem(
                    DirContext.ADD_ATTRIBUTE, new BasicAttribute(memberAttribute, uid)) };

            String[] split = distinguishedGroupName.split(",DC");
            ldapTemplate.modifyAttributes(split[0], modItems);

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Added user " + uid + " to group " + split[0]);

            return true;
        } catch (org.springframework.ldap.NameAlreadyBoundException e) {
            LOGGER.error("User " + uid + " already member of group " + groupDistinguishedName);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error on adding user to group", e);
            return false;
        }
    }

    /**
     * Remove an user in a group by DN
     * 
     * @param userDinstinguishedName
     * @param groupDistinguishedName
     * 
     * @return true if we can change or false otherwise
     */
    private boolean removeUserToGroup(String uid, String groupDistinguishedName) {
        try {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Group name: " + groupDistinguishedName);

            String distinguishedGroupName = groupDistinguishedName;

            ModificationItem[] modItems = new ModificationItem[] { new ModificationItem(
                    DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(memberAttribute, uid)) };

            String[] split = distinguishedGroupName.split(",DC");

            ldapTemplate.modifyAttributes(split[0], modItems);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Remove user " + uid + " to group " + split[0]);

            return true;
        } catch (org.springframework.ldap.NameNotFoundException e) {
            LOGGER.error("User " + uid + " already not member of group " + groupDistinguishedName);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error on adding user to group", e);
            return false;
        }
    }

    /**
     * Simple envelop for an LDAP unit mapping to be used in private methods
     * 
     * @author adiaz
     */
    private class LDAPUnit {
        private Name name;

        private Object[] members;
    }

    /**
     * @return the ldapTemplate
     */
    public LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    /**
     * @param ldapTemplate the ldapTemplate to set
     */
    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * @return the lDAP_USER_BASE_SEARCH
     */
    public String getLDAP_USER_BASE_SEARCH() {
        return LDAP_USER_BASE_SEARCH;
    }

    /**
     * @param lDAP_USER_BASE_SEARCH the lDAP_USER_BASE_SEARCH to set
     */
    public void setLDAP_USER_BASE_SEARCH(String lDAP_USER_BASE_SEARCH) {
        LDAP_USER_BASE_SEARCH = lDAP_USER_BASE_SEARCH;
    }

    /**
     * @return the lDAP_FORCE_BASE_SEARCH
     */
    public String getLDAP_FORCE_BASE_SEARCH() {
        return LDAP_FORCE_BASE_SEARCH;
    }

    /**
     * @param lDAP_FORCE_BASE_SEARCH the lDAP_FORCE_BASE_SEARCH to set
     */
    public void setLDAP_FORCE_BASE_SEARCH(String lDAP_FORCE_BASE_SEARCH) {
        LDAP_FORCE_BASE_SEARCH = lDAP_FORCE_BASE_SEARCH;
    }

    /**
     * @return the lDAP_PROPERTIES_SEARCH
     */
    public String getLDAP_PROPERTIES_SEARCH() {
        return LDAP_PROPERTIES_SEARCH;
    }

    /**
     * @param lDAP_PROPERTIES_SEARCH the lDAP_PROPERTIES_SEARCH to set
     */
    public void setLDAP_PROPERTIES_SEARCH(String lDAP_PROPERTIES_SEARCH) {
        LDAP_PROPERTIES_SEARCH = lDAP_PROPERTIES_SEARCH;
    }

    /**
     * @return the lDAP_CN_PROPERTY
     */
    public String getLDAP_CN_PROPERTY() {
        return LDAP_CN_PROPERTY;
    }

    /**
     * @param lDAP_CN_PROPERTY the lDAP_CN_PROPERTY to set
     */
    public void setLDAP_CN_PROPERTY(String lDAP_CN_PROPERTY) {
        LDAP_CN_PROPERTY = lDAP_CN_PROPERTY;
    }

    /**
     * @return the lDAP_UID_PROPERTY
     */
    public String getLDAP_UID_PROPERTY() {
        return LDAP_UID_PROPERTY;
    }

    /**
     * @param lDAP_UID_PROPERTY the lDAP_UID_PROPERTY to set
     */
    public void setLDAP_UID_PROPERTY(String lDAP_UID_PROPERTY) {
        LDAP_UID_PROPERTY = lDAP_UID_PROPERTY;
    }

    /**
     * @return the ldapPropertiesSearchSeparator
     */
    public static String getLdapPropertiesSearchSeparator() {
        return LDAP_PROPERTIES_SEARCH_SEPARATOR;
    }

    /**
     * @return the ldapPropertiesValuesSearchSeparator
     */
    public static String getLdapPropertiesValuesSearchSeparator() {
        return LDAP_PROPERTIES_VALUES_SEARCH_SEPARATOR;
    }

    /**
     * @return the memberAttribute
     */
    public String getMemberAttribute() {
        return memberAttribute;
    }

    /**
     * @param memberAttribute the memberAttribute to set
     */
    public void setMemberAttribute(String memberAttribute) {
        this.memberAttribute = memberAttribute;
    }

    /**
     * @return the parametergroup
     */
    public static String getParametergroup() {
        return parameterGroup;
    }

    /**
     * @return the parametergroupvalue
     */
    public static String getParametergroupvalue() {
        return parameterGroupValue;
    }
}
