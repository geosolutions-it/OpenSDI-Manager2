/*
 *  Copyright (C) 2013 GeoSolutions S.A.S.
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
package it.geosolutions.nrl.cip.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public abstract class BaseTest {

    private final Logger LOGGER;

    @Rule
    public TestName _testName = new TestName();
    private String testName;


    public BaseTest() {
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    public String getTestName() {
        return testName;
    }

    @Before
    public synchronized void baseTestBefore() {
        LOGGER.info("---------- ");
        LOGGER.info("---------- RUNNING TEST " + getClass().getSimpleName() + " :: " + _testName.getMethodName());
        LOGGER.info("---------- ");
    }    
}