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
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class GeoCollectUtils {
	/**
	 * Clones the given SimpleFeature using a DataStore fetch schema.
	 * 
	 * @param store
	 * @param typeName
	 * @param feature
	 * @return
	 * @throws IOException
	 */
	public static SimpleFeature cloneFeature(DataStore store, String typeName,
			SimpleFeature feature, Map<String, String> attributeMappings) throws IOException {
		SimpleFeatureType schema = store.getSchema(typeName);
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
		for(AttributeDescriptor attribute : schema.getAttributeDescriptors()) {			
			String sourceAttributeName = mapAttributeName(attributeMappings,
					attribute.getLocalName());
			builder.add(feature.getAttribute(sourceAttributeName));
		}
		return builder.buildFeature(null);
	}

	/**
	 * Returns the mapped attribute or the original one if no mapping is found
	 * @param attributeMappings
	 * @param sourceAttributeName
	 * @return
	 */
	private static String mapAttributeName(
			Map<String, String> attributeMappings, String sourceAttributeName) {
		if(attributeMappings != null && attributeMappings.containsKey(sourceAttributeName)) {
			sourceAttributeName = attributeMappings.get(sourceAttributeName);
		}
		return sourceAttributeName;
	}
}
