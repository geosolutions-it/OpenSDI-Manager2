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

import com.googlecode.genericdao.search.SearchResult;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Rule;
import it.geosolutions.opensdi2.service.RuleService;
import it.geosolutions.opensdi2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vibi/rule")
public class RuleController extends BaseController<Rule, Long> {

    @Autowired
    private RuleService ruleService;

    @Autowired
    SecurityService securityService;

    @Override
    protected RuleService getBaseService() {
        return ruleService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    CRUDResponseWrapper<Rule> list(@RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String filters,
                                   @RequestParam(required = false) String ordering,
                                   @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                   @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                   @RequestParam(required = false, defaultValue = "-1") Integer page) {
        SearchResult searchResult = getBaseService().getAll(keyword, filters, ordering, maxResults, firstResult, page);
        CRUDResponseWrapper<Rule> responseWrapper = new CRUDResponseWrapper<Rule>();
        responseWrapper.setCount(searchResult.getResult().size());
        responseWrapper.setTotalCount(searchResult.getTotalCount());
        responseWrapper.setData(searchResult.getResult());
        return responseWrapper;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public
    @ResponseBody
    void create(@RequestBody Rule rule) {
        ruleService.persist(rule);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public
    @ResponseBody
    void update(@PathVariable(value = "id") Long id, @RequestBody Rule rule) {
        rule.setId(id);
        ruleService.merge(id, rule);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public
    @ResponseBody
    void delete(@PathVariable(value = "id") Long id) {
        ruleService.delete(id);
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean check(@RequestParam(required = false) String service,
                  @RequestParam(required = false) String operation,
                  @RequestParam(required = false) String entity,
                  @RequestParam(required = false) String format,
                  @RequestParam(required = false) Integer size) {
        try {
            securityService.validate(service, operation, entity, format, size);
        } catch (SecurityException exception) {
            return false;
        }
        return true;
    }
}