/*
 *  Copyright (C) 2007 - 2014 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.httpproxy.config;

import it.geosolutions.httpproxy.callback.ProxyCallback;
import it.geosolutions.httpproxy.service.ProxyService;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * This post processor append a list of callbacks {@link #callbacksToAdd} to a specific proxy (by {@link #beanName}) or to all proxy instances in the
 * spring session
 * 
 * @author adiaz
 * 
 */
public class ProxyServiceCallbackAppenderPostProcessor implements BeanPostProcessor {

    /**
     * Bean name of the ProxyService
     */
    private String beanName;

    /**
     * Proxy callbacks to add
     */
    private List<ProxyCallback> callbacksToAdd;

    /**
     * Add callbacks to the named bean or to all proxy implementations
     * 
     * @see org.springframework.beans.factory.config.BeanPostProcessor# postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        boolean register = false;
        if (this.beanName != null) {
            register = beanName.equals(this.beanName);
        } else {
            register = true;
        }
        if (register && bean instanceof ProxyService) {
            for (ProxyCallback callback : callbacksToAdd) {
                ((ProxyService) bean).addCallback(callback);
            }
        }
        return bean;
    }

    /**
     * Empty method.
     * 
     * @see BeanPostProcessor#postProcessBeforeInitialization(Object, String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    /**
     * @return the beanName
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * @param beanName the beanName to set
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * @return the callbacksToAdd
     */
    public List<ProxyCallback> getCallbacksToAdd() {
        return callbacksToAdd;
    }

    /**
     * @param callbacksToAdd the callbacksToAdd to set
     */
    public void setCallbacksToAdd(List<ProxyCallback> callbacksToAdd) {
        this.callbacksToAdd = callbacksToAdd;
    }

}
