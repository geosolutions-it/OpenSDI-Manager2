package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Generic interface for dump and restore. Manage only a list of resources
 * @author Lorenzo Natali
 *
 * @param <T> the Type of the resource to dump/restore
 */
public interface DumpRestoreCRUDController<T> {
	
	/**
	 * Dump the resource
	 * @return the List of the resoruce
	 * @throws RESTControllerException
	 */
	@RequestMapping(value = "/dump", method = RequestMethod.GET)
	public @ResponseBody CRUDResponseWrapper<T> dump() throws RESTControllerException;
	
	/**
	 * Restore the list of resources
	 * @param lcd the list of resorces
	 * @return 
	 * @throws RESTControllerException
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody String restore(@RequestBody CRUDResponseWrapper<T> lcd) throws RESTControllerException;
}
