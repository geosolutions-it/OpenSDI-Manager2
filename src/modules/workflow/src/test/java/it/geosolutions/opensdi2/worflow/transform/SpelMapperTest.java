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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.transform.spel.SpelTransformer;
import it.geosolutions.opensdi2.workflow.transform.spel.accessors.MapPropertyAccessor;
import it.geosolutions.opensdi2.workflow.transform.spel.prebuilders.MapPreBuilder;
import it.geosolutions.opensdi2.workflow.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.PropertyAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test the Implementation of <SpelTransformer> for <Map> objects
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/mapTransformer-test.xml")
public class SpelMapperTest {
	private final static Logger LOGGER = Logger.getLogger(SpelMapperTest.class);
	@Autowired
	TestMapList testMaps;
	
	@Test
	public void testAttributeReplacement(){
		SpelTransformer<Map<String,Object>,Map<String,Object>> mapper = new SpelTransformer<Map<String,Object>,Map<String,Object>>();
		
		for(MapperTestContainer test : testMaps.getTests()){
			mapper.setRules(test.getRules());
			List<PropertyAccessor> mpa = new ArrayList<PropertyAccessor>();
			mpa.add(new MapPropertyAccessor());
			mapper.setInputaccessors(mpa);
			mapper.setOutputaccessors(mpa);
			mapper.setOutputPreBuilder(new MapPreBuilder());
			Map<String,Object> result = (Map<String, Object>) mapper.transform(test.getSource());
			checkReplacement(mapper,test,result);
		}
	}

	private void checkReplacement(SpelTransformer mapper, MapperTestContainer test, Map<String, Object> result) {
		for(String expectedPath : test.getExpected().keySet()){
			boolean positive = true;
			String searchPath = expectedPath;
			if(expectedPath.startsWith("!")){
				searchPath = expectedPath.substring(1);
				positive = false;
			}
			//this supports only . notation
			Object res = MapUtil.getAttribute(searchPath, result);
			Object exp = test.getExpected().get(expectedPath);
			
			
			if(positive){
				LOGGER.info(searchPath +" -- result:" + res + " - expected:" + exp);
				assertEquals(exp,res);
			}else{
				LOGGER.info(searchPath +" -- result:" + res + " - not desired:" + exp);
				
				assertTrue(!exp.equals(res));
			}
			
		}
		
	}

	
	

}
