package it.geosolutions.opensdi2.worflow.transform;

/*
 *  OpenSDI Manager
 *  Copyright (C) 2014 GeoSolutions S.A.S.
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
/**
 * 
 */

import it.getosolutions.opensdi2.workflow.transform.spel.SpelTransformer;
import it.getosolutions.opensdi2.workflow.transform.spel.prebuilders.DefaultPreBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test the Implementation of <SpelTransformer> for <Map> objects
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/defaultTransformer-test.xml")
public class DefaultSpelTransformerTest {
	private final static Logger LOGGER = Logger
			.getLogger(DefaultSpelTransformerTest.class);
	@Resource(name="rules") 
	Map<String, String> rules;
	DefaultSpelTestObj in;

	@Test
	public void testAttributeReplacement() {
		SpelTransformer<DefaultSpelTestObj, DefaultSpelTestObj> transformer = new SpelTransformer<DefaultSpelTestObj, DefaultSpelTestObj>();

		transformer.setRules(rules);
		List<PropertyAccessor> l = new ArrayList<PropertyAccessor>();
		l.add(new ReflectivePropertyAccessor());
		transformer.setInputaccessors(l);
		transformer.setOutputaccessors(l);
		transformer.setOutputObject(new DefaultSpelTestObj());

		DefaultSpelTestObj result = (DefaultSpelTestObj) transformer
				.transform(in);

	}
	/**
	 * Test the default perbuilder
	 */
	@Test
	public void preBuilderTest(){
		
		DefaultPreBuilder<DefaultSpelTestObj> t = new DefaultPreBuilder<DefaultSpelTestObj>(DefaultSpelTestObj.class);
		DefaultSpelTestObj obj = t.build(rules);
		SpelTransformer<DefaultSpelTestObj, DefaultSpelTestObj> transformer = new SpelTransformer<DefaultSpelTestObj, DefaultSpelTestObj>();

		transformer.setRules(rules);
		List<PropertyAccessor> l = new ArrayList<PropertyAccessor>();
		l.add(new ReflectivePropertyAccessor());
		transformer.setInputaccessors(l);
		transformer.setOutputaccessors(l);
		transformer.setOutputPreBuilder(t);

		DefaultSpelTestObj result = (DefaultSpelTestObj) transformer
				.transform(in);
		
	}

	

}
