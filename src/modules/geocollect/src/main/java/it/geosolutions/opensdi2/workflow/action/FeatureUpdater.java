/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 - 2015 GeoSolutions S.A.S.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * Reads the {@link SimpleFeature} and update a target {@link Feature}
 * based on the attribute mapping
 * If no DataStore is configured, throw a WorkflowException.
 * If the input feature isn't a {@link SimpleFeature} throw a IllegalArgumentException.
 * Throws {@link WorkflowException} if {@link IOException} occurs.
 * 
 * @author Lorenzo Pini (lorenzo.pini@geo-solutions.it)
 */
public class FeatureUpdater extends BaseAction {
    
    private String inputId = "input";
    private FeatureUpdaterConfiguration featureUpdaterConfiguration;
    private Map<String, String> attributeMappings = new HashMap<String, String>();
    
    private Map<String, String> updateMapping;
    
    @Override
    public void executeAction(WorkflowContext ctx) throws WorkflowException {
        
        Object inputObj = ctx.getContextElement(inputId);
        if(inputObj != null && inputObj instanceof SimpleFeature) {
            SimpleFeature surveyFeature = (SimpleFeature)inputObj;
            DataStore globalStore = null;
            
            if(featureUpdaterConfiguration == null || featureUpdaterConfiguration.getRules() == null) {
                //Nothing to do
                return;
            }
                
            updateMapping = featureUpdaterConfiguration.getRules();
            if(updateMapping.isEmpty()){
                //Nothing to do
                return;
            }
            
            // Create Name mapping
            ArrayList<Name> attributeNamesList = new ArrayList<Name>();
            ArrayList<Object> attributeValuesList = new ArrayList<Object>();
            
            for(String attributeName : updateMapping.keySet() ){
                
                if(surveyFeature.getProperty(attributeName) != null){
                    
                    attributeNamesList.add(new NameImpl(updateMapping.get(attributeName)));
                    attributeValuesList.add(surveyFeature.getAttribute(attributeName));
                }
            }
            
            Name[] attributeNames = attributeNamesList.toArray(new Name[attributeNamesList.size()]);
            
            Object[] attributeValues = attributeValuesList.toArray(new Object[attributeValuesList.size()]);
            
            
            Transaction transaction = new DefaultTransaction();
            try {
                globalStore = featureUpdaterConfiguration.getDataStore();
                
                if(globalStore == null) {
                    throw new WorkflowException("Cannot connect to the DataStore");
                }
                
                FilterFactory ff = CommonFactoryFinder.getFilterFactory();

                Filter filter = ff.equals(
                        ff.property(
                                featureUpdaterConfiguration.itemLinkingField ),
                                ff.literal(
                                        (String) surveyFeature.getAttribute(featureUpdaterConfiguration.surveyLinkingField)
                                        )
                        );
                
                // clone the input feature using the datastore featuretype
                // directly writing the input feature can fail because of attributes
                // mismatch
                //String typeName = getTypeName(ctx, feature);
                //feature = GeoCollectUtils.cloneFeature(store, typeName, feature, attributeMappings);
                
                FeatureStore<SimpleFeatureType, SimpleFeature> targetItemFeatureStore =  (FeatureStore<SimpleFeatureType, SimpleFeature>) 
                        globalStore.getFeatureSource(featureUpdaterConfiguration.getTypeName());
                
                // Open a transaction
                targetItemFeatureStore.setTransaction(transaction);
                
                
                targetItemFeatureStore.modifyFeatures(attributeNames, attributeValues, filter);
                
                // Commit the transaction
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
                if(globalStore != null) {
                    globalStore.dispose();
                }
            }
            
        } else {
            throw new IllegalArgumentException("Invalid input: must be a SimpleFeature object");
        }
        
        
    }

    private String getTypeName(WorkflowContext ctx, SimpleFeature feature) {
        String typeName = feature.getType().getName().getLocalPart();
        if(featureUpdaterConfiguration.getTypeNameInContext() != null && 
                ctx.getContextElement(featureUpdaterConfiguration.getTypeNameInContext()) != null) {
            typeName = (String)ctx.getContextElement(featureUpdaterConfiguration.getTypeNameInContext());
        } else if(featureUpdaterConfiguration.getTypeName() != null) { 
            typeName = featureUpdaterConfiguration.getTypeName();
        }
        return typeName;
    }
    
    
    @Override
    public void setConfiguration(BlockConfiguration config) {
        super.setConfiguration(config);
        if(config != null && config instanceof FeatureUpdaterConfiguration) {
            featureUpdaterConfiguration = (FeatureUpdaterConfiguration)config;
            if(featureUpdaterConfiguration.getInputObjectId() != null) {
                inputId = featureUpdaterConfiguration.getInputObjectId();
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
