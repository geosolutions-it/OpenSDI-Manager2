package it.geosolutions.opensdi2.mvc;

import com.googlecode.genericdao.search.SearchResult;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseController<T> {

    protected abstract BaseService<T> getBaseService();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<T> list(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String filters,
                                @RequestParam(required = false) String ordering,
                                @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                @RequestParam(required = false, defaultValue = "-1") Integer page) {
        SearchResult searchResult = getBaseService().getAll(keyword, filters, ordering, maxResults, firstResult, page);
        CRUDResponseWrapper<T> responseWrapper = new CRUDResponseWrapper<T>();
        responseWrapper.setCount(searchResult.getResult().size());
        responseWrapper.setTotalCount(searchResult.getTotalCount());
        responseWrapper.setData(searchResult.getResult());
        return responseWrapper;
    }
}
