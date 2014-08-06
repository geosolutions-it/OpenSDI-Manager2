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

import org.geotools.data.DataStore;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.FeatureCollection;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

public class DataStoreReaderTest {
	private static final String OUTPUT_ID = "output";
	DataStoreReader dataStoreReader;
	DataStore store;
	DataStoreReaderConfiguration cfg;
	WorkflowContext ctx;
	SimpleFeature feature1;
	SimpleFeature feature2;
	
	@Before
	public void setUp() throws IOException {
		ctx = new WorkflowContext();
		store = new MemoryDataStore();
		cfg = new DataStoreReaderConfiguration() {

			@Override
			public DataStore getDataStore() throws IOException {

				return store;
			}
			
		};
		cfg.setOutputObjectId(OUTPUT_ID);
		cfg.setTypeName(TestUtils.SAMPLE_FEATURE_NAME);
		dataStoreReader = new DataStoreReader();
		dataStoreReader.setConfiguration(cfg);
		
		feature1 = TestUtils.buildFeature("/geojson1.json");
		feature2 = TestUtils.buildFeature("/geojson2.json");
		
		
		TestUtils.addSchemaToStore(store, TestUtils.SAMPLE_FEATURE_NAME, TestUtils.SAMPLE_ATTRIBUTES);
		TestUtils.addFeatureToStore(store, TestUtils.SAMPLE_FEATURE_NAME, feature1);
	}
	
	@Test
	public void testReadReturnsFeatureCollectionAsOutput() throws WorkflowException {
		dataStoreReader.execute(ctx);
		Object output = ctx.getContextElement(OUTPUT_ID);
		assertNotNull(output);
		assertTrue(output instanceof FeatureCollection);
		FeatureCollection fc = (FeatureCollection)output;
		assertEquals(1, fc.size());
		SimpleFeature readFeature = (SimpleFeature)fc.features().next();
		assertEquals(feature1.getFeatureType().getName().getLocalPart(), readFeature.getName().getLocalPart());
		assertEquals(feature1.getAttribute("prop0"), readFeature.getAttribute("prop0"));
		assertEquals(feature1.getAttribute("prop1"), readFeature.getAttribute("prop1"));
		assertEquals(feature1.getAttribute("geometry"), readFeature.getAttribute("geometry"));
	}
	
	@Test
	public void testReadReadsAllFeatures() throws IOException, WorkflowException {
		TestUtils.addFeatureToStore(store, TestUtils.SAMPLE_FEATURE_NAME, feature2);
		dataStoreReader.execute(ctx);
		
		FeatureCollection fc = (FeatureCollection)ctx.getContextElement(OUTPUT_ID);
		assertEquals(2, fc.size());
	}
	
	@Test
	public void testReadAppliesFilter() throws IOException, WorkflowException {
		TestUtils.addFeatureToStore(store, TestUtils.SAMPLE_FEATURE_NAME, feature2);
		ctx.addContextElement("filter", "prop0='value0'");
		dataStoreReader.execute(ctx);
		
		FeatureCollection fc = (FeatureCollection)ctx.getContextElement(OUTPUT_ID);
		assertEquals(1, fc.size());
	}
	
	@Test
	public void testReadCanGetTypeNameFromContext() throws WorkflowException {
		cfg.setTypeNameInContext("typename");
		ctx.addContextElement("typename", TestUtils.SAMPLE_FEATURE_NAME);
		cfg.setTypeName(null);
		dataStoreReader.execute(ctx);
		Object output = ctx.getContextElement(OUTPUT_ID);
		assertNotNull(output);
	}
}
