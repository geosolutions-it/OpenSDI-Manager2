package it.geosolutions.opensdi2.worflow;
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

import static org.junit.Assert.*;
import it.geosolutions.opensdi2.workflow.ActionSequence;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.getosolutions.opensdi2.workflow.transform.spel.SpelTransformer;
import it.getosolutions.opensdi2.workflow.transform.spel.accessors.MapPropertyAccessor;
import it.getosolutions.opensdi2.workflow.transform.spel.prebuilders.MapPreBuilder;
import it.getosolutions.opensdi2.workflow.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.PropertyAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test the Implementation of <SpelTransformer> for <Map> objects and reflection, using the context
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/actionSequence-test.xml")
public class ActionSequenceTest {
	

	@Autowired
	ActionSequence sequence1;
	
	@Autowired
	ActionSequence sequence2;
	
	@Resource
	Map<String,Object> map1;
	@Test
	public void actionSequenceTest() throws IllegalArgumentException, WorkflowException{
		
		
			WorkflowContext ctx = new WorkflowContext();
			//
			//run first action sequence		
			//
			
			//get input
			ctx.addContextElement("map", map1);
			assertNotNull(sequence1);
			assertEquals("sequence1", sequence1.getId());
			assertNotNull(sequence1.getActions());
			//execute sequence
			sequence1.execute(ctx);
			//test results
			Map<String,Object> out= (Map<String,Object>)ctx.getContextElement("out2");
			assertNotNull(out);
			assertEquals("second1", out.get("out1"));
			assertEquals("replace1", out.get("out2"));
			assertEquals(6, out.get("out4"));
			assertEquals(5, out.get("out5"));
			
			//the second sequence doesn't specify input, it the whole context
			ctx = new WorkflowContext();
			ctx.addContextElement("map", map1);
			sequence2.execute(ctx);
			out= (Map<String,Object>)ctx.getContextElement("out2");
			assertEquals("mockMap1.test1", out.get("out1"));
			assertEquals("replace1", out.get("out2"));
			assertEquals("second3", out.get("out3"));
			
	}
	
}
