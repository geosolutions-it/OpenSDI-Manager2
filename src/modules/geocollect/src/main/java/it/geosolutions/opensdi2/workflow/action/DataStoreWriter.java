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
import it.geosolutions.opensdi2.workflow.utils.GeoCollectUtils;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class DataStoreWriter extends BaseAction {
	private String inputId = "input";
	private DataStoreConfiguration dataStoreConfiguration;
		
	
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		
		Object inputObj = ctx.getContextElement(inputId);
		if(inputObj != null && inputObj instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature)inputObj;
			DataStore store = null;
			Transaction transaction = new DefaultTransaction();
			try {
				store = dataStoreConfiguration.getDataStore();
				
				if(store == null) {
					throw new WorkflowException("Cannot connect to the DataStore");
				}
				
				// clone the input feature using the datastore featuretype
				// directly writing the input feature can fail because of attributes
				// mismatch
				String typeName = getTypeName(ctx, feature);
				feature = GeoCollectUtils.cloneFeature(store, typeName, feature);
				
				FeatureStore<SimpleFeatureType, SimpleFeature> featureStore =  (FeatureStore<SimpleFeatureType, SimpleFeature>) 
						store.getFeatureSource(feature.getFeatureType().getName());
				featureStore.setTransaction(transaction);
				featureStore.addFeatures(DataUtilities.collection(feature));
				transaction.commit();
				
				
			} catch (IOException e) {
				try {
					transaction.rollback();
				} catch (IOException e1) {
					
				}
				throw new WorkflowException("Error writing to the underlying DataStore", e);
			} finally {
				try {
					transaction.close();
				} catch (IOException e) {
					
				}
				if(store != null) {
					store.dispose();
				}
			}
			
		} else {
			throw new IllegalArgumentException("Invalid input: must be a SimpleFeature object");
		}
		
		
	}

	private String getTypeName(WorkflowContext ctx, SimpleFeature feature) {
		String typeName = feature.getType().getName().getLocalPart();
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
		if(config != null && config instanceof DataStoreConfiguration) {
			dataStoreConfiguration = (DataStoreConfiguration)config;
			if(dataStoreConfiguration.getInputObjectId() != null) {
				inputId = dataStoreConfiguration.getInputObjectId();
			}
		}
	}

}
