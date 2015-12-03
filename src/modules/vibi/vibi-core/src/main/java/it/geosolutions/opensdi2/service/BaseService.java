package it.geosolutions.opensdi2.service;

import com.googlecode.genericdao.search.Search;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.Plot;

import java.util.List;

public abstract class BaseService<T> {

    protected abstract GenericVibiDao getDao();

    public List<T> getAll(String keywordString, String filtersString,
                             String sortingString, int maxResults, int firstResult, int page) {
        Search search = new Search(getDao().getEntityType());
        if(keywordString != null) {
            search.addFilter(getDao().getKeyWordSearchFilter(keywordString));
        }
        search.setMaxResults(maxResults);
        search.setFirstResult(firstResult);
        search.setPage(page);
        return getDao().search(search);
    }

    public List<T> getAll() {
        return getDao().findAll();
    }
}
