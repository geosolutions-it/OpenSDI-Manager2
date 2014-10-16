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
package it.geosolutions.opensdi2.workflow.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Point;

public class TestUtils {
	private static FeatureJSON jsonReader = new FeatureJSON();
	
	public static final String SAMPLE_FEATURE_NAME = "feature";
	
	public static Map<String,Class> SAMPLE_ATTRIBUTES = new HashMap<String, Class>();
	
	static {
		SAMPLE_ATTRIBUTES.put("prop0", String.class);
		SAMPLE_ATTRIBUTES.put("prop1", Double.class);
		SAMPLE_ATTRIBUTES.put("geometry", Point.class);
	}
	
	public static String readResource(String resourceName) throws IOException {
		return IOUtils.toString(TestUtils.class.getResource(resourceName).openStream());
	}

	public static SimpleFeature buildFeature(String fileName) throws IOException {
		return jsonReader.readFeature(TestUtils.readResource(fileName));
	}
	
	public static void addSchemaToStore(DataStore store, String typeName, Map<String, Class> attributes) throws IOException {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName(typeName);
		for(String propName : attributes.keySet()) {
			typeBuilder.add(propName, attributes.get(propName));
		}
		store.createSchema(typeBuilder.buildFeatureType());
	}

	/**
	 * 
	 * @param store
	 * @param typeName
	 * @param feature
	 * @throws IOException
	 */
	public static void addFeatureToStore(DataStore store,
			String typeName, SimpleFeature feature) throws IOException {
		
		((FeatureStore)store.getFeatureSource(typeName)).addFeatures(
				DataUtilities.collection(GeoCollectUtils.cloneFeature(store, typeName, feature, new HashMap<String, String>())));
		
	}

	/**
	 * 
	 * @param store
	 * @param typeName
	 * @param feature
	 * @param testMapping
	 * @throws IOException
	 */
	public static void addFeatureToStoreWithMapping(DataStore store,
			String typeName, SimpleFeature feature, Map<String, String> testMapping) throws IOException {
		
		((FeatureStore)store.getFeatureSource(typeName)).addFeatures(
				DataUtilities.collection(GeoCollectUtils.cloneFeature(store, typeName, feature, testMapping)));
	}

	
}
