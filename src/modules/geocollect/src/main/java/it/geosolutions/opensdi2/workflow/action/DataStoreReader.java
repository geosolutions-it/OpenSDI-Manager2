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

import it.geosolutions.opensdi2.workflow.BaseAction;
import it.geosolutions.opensdi2.workflow.BlockConfiguration;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class DataStoreReader extends BaseAction {
	private DataStoreReaderConfiguration dataStoreConfiguration;
	private String outputId = "output";
	private String filterId = "filter";
	
	@Override
	protected void executeAction(WorkflowContext ctx)
			throws WorkflowException {
		String typeName = getTypeName(ctx);
		if(typeName == null) {
			throw new WorkflowException("Cannot find the input typeName to be read");
		}
		DataStore store = null;
		
		try {
			store = dataStoreConfiguration.getDataStore();
			
			if(store == null) {
				throw new WorkflowException("Cannot connect to the DataStore");
			}
			
			FeatureStore<SimpleFeatureType, SimpleFeature> featureStore =  (FeatureStore<SimpleFeatureType, SimpleFeature>) 
					store.getFeatureSource(typeName);
			Filter filter = getFilter(ctx);
			FeatureCollection<SimpleFeatureType, SimpleFeature> features = null;
			if(filter == null) {
				features= featureStore.getFeatures();
			} else {
				features= featureStore.getFeatures(filter);
			}
			ctx.addContextElement(outputId, DataUtilities.collection(features));
			
			
			
		} catch (IOException e) {
			throw new WorkflowException("Error reading from the underlying DataStore", e);
		} catch (CQLException e) {
			throw new WorkflowException("Error parsing the CQL filter", e);
		} finally {
			if(store != null) {
				store.dispose();
			}
		}
	}

	private Filter getFilter(WorkflowContext ctx) throws CQLException {
		if(ctx.getContextElement(filterId) != null) {
			return CQL.toFilter(ctx.getContextElement(filterId).toString());
		}
		return null;
	}

	private String getTypeName(WorkflowContext ctx) {
		String typeName = null;
		if(dataStoreConfiguration.getTypeNameInContext() != null && 
				ctx.getContextElement(dataStoreConfiguration.getTypeNameInContext()) != null) {
			typeName = (String)ctx.getContextElement(dataStoreConfiguration.getTypeNameInContext());
		} else if(dataStoreConfiguration.getTypeName() != null) { 
			typeName = dataStoreConfiguration.getTypeName();
		}
		return typeName;
	}
	
	@Override
	public void setConfiguration(BlockConfiguration config) {
		super.setConfiguration(config);
		if(config != null && config instanceof DataStoreReaderConfiguration) {
			dataStoreConfiguration = (DataStoreReaderConfiguration)config;
			if(dataStoreConfiguration.getOutputObjectId() != null) {
				outputId = dataStoreConfiguration.getOutputObjectId();
			}
			if(dataStoreConfiguration.getFilterId() != null) {
				filterId = dataStoreConfiguration.getFilterId();
			}
		}
	}
}
