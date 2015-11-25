package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Simple Generic REST controller with basic CRUD operations for an entity.
 * 
 * 
 * @author Lorenzo Natali, GeoSolutions
 * 
 * @param <T> the Type of the Entity
 * @param <IDTYPE> The Type of the Resource Identifier
 */
public interface SimpleEntityCRUDController<T,IDTYPE> {
	
	/**
	 * @return
	 * @throws RESTControllerException
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody CRUDResponseWrapper<T> list() throws RESTControllerException;
	
	/**
	 * 
	 * @param c
	 * @return
	 * @throws RESTControllerException
	 */
	@RequestMapping(value="/", method = RequestMethod.POST)
	public @ResponseBody IDTYPE  create(@RequestBody T c) throws RESTControllerException;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody T get(@PathVariable(value = "id") IDTYPE  id ) throws RESTControllerException;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody IDTYPE update(@PathVariable(value = "id") String  id,@RequestBody T c) throws RESTControllerException;
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody IDTYPE update(@RequestBody T c) throws RESTControllerException;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@PathVariable(value = "id") IDTYPE  id) throws RESTControllerException;

}
