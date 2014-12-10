/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import it.geosolutions.opensdi2.configurations.controller.OSDIModuleController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author alessio.fabiani
 * 
 */
@RequestMapping("/process")
public abstract class RestAPIBaseController extends OSDIModuleController {

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
