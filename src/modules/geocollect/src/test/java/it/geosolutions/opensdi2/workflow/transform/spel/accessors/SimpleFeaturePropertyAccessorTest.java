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
package it.geosolutions.opensdi2.workflow.transform.spel.accessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.transform.spel.accessors.SimpleFeaturePropertyAccessor;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.geotools.geojson.feature.FeatureJSON;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class SimpleFeaturePropertyAccessorTest {
	SimpleFeaturePropertyAccessor accessor;
	SimpleFeature feature;
	EvaluationContext ctx;
	GeometryFactory gf = new GeometryFactory();
	Geometry sampleGeometry;
	
	
	
	@Before
	public void setUp() throws IOException {
		feature = TestUtils.buildFeature("/geojson1.json");
		accessor = new SimpleFeaturePropertyAccessor();
		sampleGeometry = gf.createPoint(new Coordinate(1.0, 2.0));
	}
	
	@Test
	public void testAccessorReadsProperty() throws AccessException {
		TypedValue value = accessor.read(ctx, feature, "prop0");
		assertNotNull(value.getValue());
		assertEquals("value0", value.getValue());
	}
	
	@Test
	public void testAccessorReadsNumericProperty() throws AccessException {
		TypedValue value = accessor.read(ctx, feature, "prop1");
		assertNotNull(value.getValue());
		assertEquals(100.0, value.getValue());
	}
	
	@Test
	public void testAccessorReadsGeometry() throws AccessException {
		TypedValue value = accessor.read(ctx, feature, "geometry");
		assertNotNull(value.getValue());
		assertTrue(value.getValue() instanceof Geometry);
	}
	
	@Test
	public void testAccessorWritesProperty() throws AccessException {
		accessor.write(ctx, feature, "prop0", "value1");
		assertEquals("value1", feature.getAttribute("prop0"));
	}
	
	@Test
	public void testAccessorWritesNumericProperty() throws AccessException {
		accessor.write(ctx, feature, "prop1", 200.5);
		assertEquals(200.5, feature.getAttribute("prop1"));
	}
	
	@Test
	public void testAccessorWritesGeometryProperty() throws AccessException {
		accessor.write(ctx, feature, "geometry", sampleGeometry);
		assertEquals(sampleGeometry, feature.getAttribute("geometry"));
	}
	
	@Test
	public void testAccessorCanReadSimpleFeature() throws AccessException {
		assertTrue(accessor.canRead(ctx, feature, ""));
	}
	
	@Test
	public void testAccessorCanWriteSimpleFeature() throws AccessException {
		assertTrue(accessor.canWrite(ctx, feature, "prop0"));
	}
	
	@Test
	public void testAccessorCannotWriteUnexistingProperty() throws AccessException {
		assertFalse(accessor.canWrite(ctx, feature, "missing"));
	}
	
}
