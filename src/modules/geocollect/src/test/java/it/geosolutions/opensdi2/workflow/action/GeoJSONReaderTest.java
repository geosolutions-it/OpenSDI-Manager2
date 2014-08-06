/*
 *  OpenSDI Manager 2
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
package it.geosolutions.opensdi2.workflow.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;



public class GeoJSONReaderTest {
	private static final String GEOJSON_RESOURCE = "/geojson1.json";
	private static final String INPUT_ID = "input";
	private static final String OUTPUT_ID = "output";
	private static final String WRONG_GEOJSON_RESOURCE = "/wrong_geojson.json";
	GeoJSONReader reader;
	WorkflowContext ctx;
	InputOutputConfiguration cfg;
	

	@Before
	public void setUp() {
		ctx = new WorkflowContext();
		cfg = new InputOutputConfiguration();
		cfg.setInputObjectId(INPUT_ID);
		cfg.setOutputObjectId(OUTPUT_ID);
		reader = new GeoJSONReader();
		reader.setConfiguration(cfg);
	}
	@Test
	public void testReadCreatesOutput() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
		reader.execute(ctx);
		assertNotNull(ctx.getContextElement(OUTPUT_ID));
	}
	
	@Test
	public void testReadCreatesSimpleFeature() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
		reader.execute(ctx);
		assertTrue(ctx.getContextElement(OUTPUT_ID) instanceof SimpleFeature);
	}
	
	@Test
	public void testReadReadsGeoJSONProperties() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
		reader.execute(ctx);
		SimpleFeature feature = (SimpleFeature)ctx.getContextElement(OUTPUT_ID);
		assertNotNull(feature.getAttribute("prop0"));
		assertEquals("value0", feature.getAttribute("prop0"));
	}
	
	@Test
	public void testReadReadsGeoJSONNumbers() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
		reader.execute(ctx);
		SimpleFeature feature = (SimpleFeature)ctx.getContextElement(OUTPUT_ID);
		assertNotNull(feature.getAttribute("prop1"));
		assertEquals(100.0, feature.getAttribute("prop1"));
	}
	
	@Test
	public void testReadReadsGeoJSONGeometry() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
		reader.execute(ctx);
		SimpleFeature feature = (SimpleFeature)ctx.getContextElement(OUTPUT_ID);
		assertNotNull(feature.getDefaultGeometry());
		assertTrue(feature.getDefaultGeometry() instanceof Geometry);
		assertTrue(feature.getDefaultGeometry() instanceof Point);
	}
	
	@Test(expected=WorkflowException.class)
	public void testErrorOnMissingInput() throws WorkflowException {
		reader.execute(ctx);
	}
	
	@Test(expected=WorkflowException.class)
	public void testErrorOnInvalidGeoJSON() throws IOException, IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(WRONG_GEOJSON_RESOURCE));
		reader.execute(ctx);
	}
	
	
}
