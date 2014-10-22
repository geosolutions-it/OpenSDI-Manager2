/**
 * 
 */
package it.geosolutions.opensdi2.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author alessio.fabiani
 * 
 */
public class OpenSDIManagerConfigExtensions implements ApplicationContextAware,
		ApplicationListener {

	/**
	 * logger
	 */
	protected static final Logger LOGGER = Logger
			.getLogger("it.geosolutions.opensdi2.config");

	static boolean isSpringContext = true;

	/**
	 * A static application context
	 */
	@Autowired
	static ApplicationContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		isSpringContext = true;
		OpenSDIManagerConfigExtensions.context = context;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public static final <T> List<T> extensions(String scope) {
		String[] names = extensionNames(ProxyFactoryBean.class);

		if (names == null) {
			return Collections.EMPTY_LIST;
		}

		// look up all the beans
		List<T> result = new ArrayList<T>(names.length);
		for (String name : names) {
			if (name.equals(scope) || name.equals("&" + scope)) {
				Object bean = context.getBean(scope);
				result.add((T) bean);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static final <T> List<T> extensions(Class clazz) {
		String[] names = extensionNames(clazz);

		if (names == null) {
			return Collections.EMPTY_LIST;
		}

		// look up all the beans
		List<T> result = new ArrayList<T>(names.length);
		for (String name : names) {
			Object bean = context.getBean(name);
			result.add((T) bean);
		}

		return result;
	}

	/**
	 * @return
	 */
	private static String[] extensionNames(Class clazz) {
		String[] names;
		if (context != null) {
			try {
				names = context.getBeanNamesForType(clazz);
				if (names == null) {
					names = new String[0];
				}
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "bean lookup error", e);
				return null;
			}
		} else {
			return null;
		}
		return names;
	}
}
