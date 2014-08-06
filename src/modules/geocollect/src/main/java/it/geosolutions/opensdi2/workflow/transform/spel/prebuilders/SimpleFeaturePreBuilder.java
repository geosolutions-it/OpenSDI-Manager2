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

import it.geosolutions.opensdi2.workflow.action.DataStoreConfiguration;
import it.getosolutions.opensdi2.workflow.transform.PreBuilder;

import java.io.IOException;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class SimpleFeaturePreBuilder implements PreBuilder<SimpleFeature>{
	private SimpleFeatureBuilder builder = null;
	private DataStore store = null;
	private String typeName = null;
	
	public SimpleFeaturePreBuilder(String name, Map<String, Class> attributes) {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName(name);
		for(String propertyName : attributes.keySet()) {
			Class binding = attributes.get(propertyName);
			typeBuilder.add(propertyName, binding);
			if(Geometry.class.isAssignableFrom(binding)) {
				typeBuilder.setDefaultGeometry(propertyName);
			}
		}
		builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
	}
	
	public SimpleFeaturePreBuilder(SimpleFeatureType schema) {
		builder = new SimpleFeatureBuilder(schema);
	}

	public SimpleFeaturePreBuilder(DataStore store, String typeName) throws IOException {
		this.store = store;
		this.typeName = typeName;
	}
	
	public SimpleFeaturePreBuilder(DataStoreConfiguration cfg, String typeName) throws IOException {
		this(cfg.getDataStore(), typeName);
	}
	
	@Override
	public SimpleFeature build(Map<String, String> rules) {
		if(builder == null) {
			try {
				createBuilder();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if(builder != null) {
			return builder.buildFeature(null);
		}
		return null;
	}

	private void createBuilder() throws IOException {
		if(store != null && typeName != null) {
			builder = new SimpleFeatureBuilder(store.getSchema(typeName));
		}
	}

	@Override
	public SimpleFeature build(SimpleFeature outputObject,
			Map<String, String> rules) {
		return outputObject;
	}

}
