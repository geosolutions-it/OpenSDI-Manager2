/**
 * 
 */
package it.geosolutions.opensdi2.config.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author alessio.fabiani
 *
 */
public class OpenSDIManagerConfigBeforeMethod implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println(" -- Accessing OpenSDI2-Manager config: " + method.getName());
	}

}
