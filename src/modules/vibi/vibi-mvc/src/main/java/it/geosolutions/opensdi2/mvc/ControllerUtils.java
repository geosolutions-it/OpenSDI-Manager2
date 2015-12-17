package it.geosolutions.opensdi2.mvc;

import com.googlecode.genericdao.search.SearchResult;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.service.BaseService;

public final class ControllerUtils {

    private ControllerUtils() {
    }

    public static <T> CRUDResponseWrapper<T> getAll(BaseService<T> baseService, String keyword,
                                                    Integer maxResults, Integer firstResult, Integer page) {
        SearchResult searchResult = baseService.getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<T> responseWrapper = new CRUDResponseWrapper<T>();
        responseWrapper.setCount(searchResult.getResult().size());
        responseWrapper.setTotalCount(searchResult.getTotalCount());
        responseWrapper.setData(searchResult.getResult());
        return responseWrapper;
    }
}
