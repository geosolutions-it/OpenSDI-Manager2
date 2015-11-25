package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Interface for controllers that provide pagination functionalities
 * @author nali
 *
 * @param <T>
 */
public interface PagebleEntityCRUDController<T>{
	/**
	 * @return
	 * @throws RESTControllerException
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody CRUDResponseWrapper<T> list( @RequestParam("start") Integer start, @RequestParam("limit") Integer limit ) throws RESTControllerException;
}
