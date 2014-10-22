package it.geosolutions.opensdi2.crud.api;

import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Generic Controller for REST Operations on application Entities.
 * Implements Exception Managements
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public abstract class CRUDControllerBase<T,IDTYPE> {

	/**
	 * Handler for exceptions. Runs when Exception of type defined is trown by a RequestMapping annotated metod.
	 * @param req the request
	 * @param exception the exception to handle
	 * @return the exception message
	 */
	@ExceptionHandler({ RESTControllerException.class})
	@ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation") 
	public @ResponseBody String conflict(RESTControllerException exception){
		return exception.getMessage();
	}
	
	
}
