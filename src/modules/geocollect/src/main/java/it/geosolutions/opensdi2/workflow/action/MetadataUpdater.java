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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Updates an input {@link SimpleFeature} properties adding:
 *  - Username
 *  - Date
 * If the input feature isn't a {@link SimpleFeature} throw a IllegalArgumentException.
 * Throws {@link WorkflowException} if {@link IOException} occurs.
 * 
 * @author Lorenzo Pini (lorenzo.pini@geo-solutions.it)
 */
public class MetadataUpdater extends BaseAction {
	
	public static String CTX_METADATA_USERNAME = "USERNAME";
	
	public static String METADATA_USERNAME = "gc_up_user";
	public static String METADATA_TIMESTAMP = "gc_created";
	
	private String inputId = "input";
	private DataStoreConfiguration dataStoreConfiguration;
	private Map<String, String> attributeMappings = new HashMap<String, String>();	
	
	@Override
	public void executeAction(WorkflowContext ctx) throws WorkflowException {
		
		Object inputObj = ctx.getContextElement(inputId);
		if(inputObj != null && inputObj instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature)inputObj;
			DataStore store = null;
			
			// Create a new feature with the correct output attributes
			try {
				store = dataStoreConfiguration.getDataStore();

				if(store == null) {
					throw new WorkflowException("Cannot connect to the DataStore");
				}
				
				String typeName = getTypeName(ctx, feature);
				feature = GeoCollectUtils.cloneFeature(store, typeName, feature, attributeMappings);
				
			} catch (IOException e) {
				throw new WorkflowException("Error writing to the underlying DataStore", e);
			}
			
			
			SimpleFeatureType sfType = feature.getFeatureType();
			if(sfType != null){
				
				if(sfType.indexOf(METADATA_USERNAME) > -1 ){
					Object username = ctx.getContextElement(CTX_METADATA_USERNAME);
					if(username != null){
						// Add the current logged User
						feature.setAttribute(METADATA_USERNAME, username);
					}
				}
				
				if(sfType.indexOf(METADATA_TIMESTAMP) > -1 ){
					// Add the current time
					feature.setAttribute(METADATA_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
				}
			}
			
			ctx.addContextElement(inputId, feature);
			
		} else {
			throw new IllegalArgumentException("Invalid input: must be a SimpleFeature object");
		}
	}

	/**
	 * Returns the typename of the given {@link SimpleFeature}
	 * @param ctx
	 * @param feature
	 * @return
	 */
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

	public Map<String, String> getAttributeMappings() {
		return attributeMappings;
	}

	public void setAttributeMappings(Map<String, String> attributeMappings) {
		this.attributeMappings = attributeMappings;
	}
	
	

}
