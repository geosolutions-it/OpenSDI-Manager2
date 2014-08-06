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
package it.geosolutions.opensdi2.workflow.transform.spel.prebuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;

public class SimpleFeaturePreBuilderTest {
	private static final String SAMPLE_FEATURE_NAME = "test";
	SimpleFeaturePreBuilder builder;
	DataStore store;
	static Map<String,Class> attributes = new HashMap<String, Class>();
	
	static {
		attributes.put("prop0", String.class);
		attributes.put("prop1", Double.class);
		attributes.put("geometry", Point.class);
	}
	
	@Before
	public void setUp() throws IOException {
		builder = new SimpleFeaturePreBuilder(SAMPLE_FEATURE_NAME, attributes);
		store = new MemoryDataStore();
		TestUtils.addSchemaToStore(store, SAMPLE_FEATURE_NAME, attributes);
	}
	
	@Test
	public void testPrebuildCreatesSimpleFeature() {
		assertNotNull(builder.build(null));
	}
	
	@Test
	public void testPrebuildCreatesSimpleFeatureFromDatastore() throws IOException {
		builder = new SimpleFeaturePreBuilder(store, SAMPLE_FEATURE_NAME);
		assertNotNull(builder.build(null));
	}
	
	@Test
	public void testPrebuildCreatesSchema() {
		SimpleFeature feature = builder.build(null);
		SimpleFeatureType featureType = feature.getFeatureType();
		
		assertNotNull(featureType);
		assertNotNull(featureType.getDescriptor("prop0"));
		assertEquals(String.class, featureType.getDescriptor("prop0").getType().getBinding());
		assertNotNull(featureType.getDescriptor("prop1"));
		assertEquals(Double.class, featureType.getDescriptor("prop1").getType().getBinding());
		assertNotNull(featureType.getDescriptor("geometry"));		
		assertEquals(Point.class, featureType.getDescriptor("geometry").getType().getBinding());

		assertNull(featureType.getDescriptor("missing"));
	}
}
