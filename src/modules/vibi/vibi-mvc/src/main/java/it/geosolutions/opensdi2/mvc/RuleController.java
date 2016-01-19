package it.geosolutions.opensdi2.mvc;

import com.googlecode.genericdao.search.SearchResult;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Rule;
import it.geosolutions.opensdi2.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vibi/rule")
public class RuleController extends BaseController<Rule, Long> {

    @Autowired
    private RuleService ruleService;

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
}