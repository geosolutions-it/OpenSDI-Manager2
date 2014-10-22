package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public interface QuerableCRUDController<T,FILTERTYPE> {
	/**
	 * Get a list of the search result
	 * @param query the query
	 * @return the search results
	 * @throws RESTControllerException
	 */
	@RequestMapping(value="/find")
	public @ResponseBody CRUDResponseWrapper<T> find( @RequestParam("filter") FILTERTYPE query ) throws RESTControllerException;
	
	/**
	 * Simple text search
	 * @param attName optional attribute name from where to apply. If null predefined text search
	 * @param valueLike the query string
	 * @return the search results
	 * @throws RESTControllerException
	 */
	@RequestMapping(value= "/filterby/", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody CRUDResponseWrapper<T> find( @RequestParam("attributename") String attName, @RequestParam("valuelike") String valueLike ) throws RESTControllerException;
}
