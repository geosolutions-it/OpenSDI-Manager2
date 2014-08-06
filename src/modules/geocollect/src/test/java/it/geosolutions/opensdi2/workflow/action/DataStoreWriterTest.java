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
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.WorkflowStatus;
import it.geosolutions.opensdi2.workflow.transform.spel.prebuilders.SimpleFeaturePreBuilder;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Point;

public class DataStoreWriterTest {
	
	
	SimpleFeaturePreBuilder builder;
	
	
	
	private static final String WRITER_ID = "writer";
	private static final String INPUT_ID = "input";
	
	private WorkflowContext ctx;
	private DataStoreWriter dataStoreWriter;
	private SimpleFeature feature;
	DataStoreConfiguration cfg;
	DataStore store;
	
	@Before
	public void setUp() throws IOException {
		ctx = new WorkflowContext();
		store = new MemoryDataStore();
		cfg = new DataStoreConfiguration() {

			@Override
			public DataStore getDataStore() throws IOException {

				return store;
			}
			
		};
		cfg.setInputObjectId(INPUT_ID);
		cfg.setTypeName(TestUtils.SAMPLE_FEATURE_NAME);
		dataStoreWriter = new DataStoreWriter();
		dataStoreWriter.setId(WRITER_ID);
		dataStoreWriter.setConfiguration(cfg);
		
		feature = TestUtils.buildFeature("/geojson1.json");
		
		TestUtils.addSchemaToStore(store, TestUtils.SAMPLE_FEATURE_NAME, TestUtils.SAMPLE_ATTRIBUTES);
	}
	@Test
	public void testWrite() throws IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, feature);
		
		dataStoreWriter.execute(ctx);
		assertTrue(ctx.getStatusElements().containsKey(WRITER_ID));
		
		assertEquals(WorkflowStatus.Status.COMPLETED, 
				ctx.getStatusElements().get(WRITER_ID).getCurrentStatus());
	}
	
	@Test(expected=WorkflowException.class)
	public void testWriterAcceptsWantsSimpleFeatureAsInput() throws IllegalArgumentException, WorkflowException {
		ctx.addContextElement(INPUT_ID, "feature");
		dataStoreWriter.execute(ctx);
	}
	
	@Test(expected=WorkflowException.class)
	public void testWriterRequiresAnInput() throws IllegalArgumentException, WorkflowException {
		dataStoreWriter.execute(ctx);
	}
	
	@Test
	public void testWriteData() throws IllegalArgumentException, WorkflowException, IOException {
		ctx.addContextElement(INPUT_ID, feature);
		dataStoreWriter.execute(ctx);
		SimpleFeatureSource featureSource = store.getFeatureSource(TestUtils.SAMPLE_FEATURE_NAME);
		FeatureCollection fc = featureSource.getFeatures();
		assertEquals(1, fc.size());
		SimpleFeature readFeature = (SimpleFeature) fc.features().next();
		assertEquals(feature.getFeatureType().getName().getLocalPart(), readFeature.getName().getLocalPart());
		assertEquals(feature.getAttribute("prop0"), readFeature.getAttribute("prop0"));
		assertEquals(feature.getAttribute("prop1"), readFeature.getAttribute("prop1"));
		assertEquals(feature.getAttribute("geometry"), readFeature.getAttribute("geometry"));
	}
}
