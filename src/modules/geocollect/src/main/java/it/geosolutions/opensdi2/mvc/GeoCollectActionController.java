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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.geocollect.model.http.CommitResponse;
import it.geosolutions.geocollect.model.http.Status;
import it.geosolutions.opensdi2.configurations.controller.OSDIModuleController;
import it.geosolutions.opensdi2.configurations.exceptions.OSDIConfigurationException;
import it.geosolutions.opensdi2.configurations.model.OSDIConfigurationKVP;
import it.geosolutions.opensdi2.workflow.ActionBlock;
import it.geosolutions.opensdi2.workflow.ActionSequence;
import it.geosolutions.opensdi2.workflow.BaseAction;
import it.geosolutions.opensdi2.workflow.BlockConfiguration;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.WorkflowStatus;
import it.geosolutions.opensdi2.workflow.action.FeatureUpdater;
import it.geosolutions.opensdi2.workflow.action.FeatureUpdaterConfiguration;
import it.geosolutions.opensdi2.workflow.action.MetadataUpdater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.opengis.filter.identity.FeatureId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Controller for a GeoCollect Action 
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@Controller
@RequestMapping("/geocollect/action")
@PreAuthorize("!hasRole('ROLE_ANONYMOUS')")
public class GeoCollectActionController extends OSDIModuleController {
	
    @Autowired
    private ApplicationContext appContext;
    
	/**
	 * Context object to store data to
	 */
	private WorkflowContext ctx;
	
	public static String GEOCOLLECT_SCOPEID = "geocollect";
	
	/**
	 * Custom IDs for the workflow
	 */
	private static final String INPUT_ID = "input";
	private static final String WRITER_ID = "writer";
	private static final String OUTPUTIDS = "outputids";

	/**
	 * Available actions
	 */
	public static final String STORE_FEATURE_ACTION = "store";

	/**
	 * Mapping of the various configured actions to run
	 */
	@Autowired
	private Map<String, ActionSequence> actionsMapping;
	
	/**
	 * Mapping of the various configurable actions
	 */
	@Autowired
	private Map<String, BaseAction> configurableAction;
	
	/**
	 * Logger
	 */
	private final static Logger LOGGER = Logger.getLogger(GeoCollectActionController.class);
	
	/**
	 * Custom Exception to return a status 404 response
	 * @author Lorenzo Pini (lorenzo.pini@geo-solutions.it)
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {

        /**
         * 
         */
        private static final long serialVersionUID = -6799140966456807323L;

	}

	
	
	@RequestMapping(value = "/{actionsequence}/configuration", method = { RequestMethod.GET })
	public @ResponseBody Object getConfigurationList(
	       @PathVariable("actionsequence") String actionSequenceId){

	    LOGGER.info("Received call for Action Configuration:" + actionSequenceId);
	    
        //////////////// BEGIN TEST  /////////////////
        if(actionsMapping != null && actionsMapping.containsKey(actionSequenceId)){
            ActionSequence sequence = actionsMapping.get(actionSequenceId);
            
            for(ActionBlock actionBlock : sequence.getActions()){
                
                if(actionBlock != null && actionBlock instanceof FeatureUpdater){
                    
                    BlockConfiguration targetConfiguration = actionBlock.getConfiguration();
                    if(targetConfiguration != null){
                        
                        LOGGER.info("Retrieved BlockConfiguration of "+actionBlock.getId()+" from actionsMapping");

                        if(targetConfiguration instanceof FeatureUpdaterConfiguration){
                            
                            try {
                                
                                OSDIConfigurationKVP actionConfig = (OSDIConfigurationKVP) depot.loadExistingConfiguration(GEOCOLLECT_SCOPEID, actionBlock.getId());
                                
                                if(targetConfiguration instanceof FeatureUpdaterConfiguration){
                                    
                                    FeatureUpdaterConfiguration fconfig = (FeatureUpdaterConfiguration) targetConfiguration;
                                    
                                    Map<String,String> rules = new HashMap<String, String>();
                                    
                                    for(String rule_key : actionConfig.getAllKeys()){
                                        
                                        if(actionConfig.getValue(rule_key) instanceof String){
                                        
                                            rules.put(rule_key, (String) actionConfig.getValue(rule_key));
                                        }
                                        
                                    }
                                    
                                    fconfig.setRules(rules);
                                    
                                    LOGGER.info("Done from actionsMapping");
                                    
                                    return fconfig;
                                }
                                
                            } catch (OSDIConfigurationException e) {
                                
                                LOGGER.warn("Error Loading configuration", e);
                                throw new ResourceNotFoundException();
                                /*
                                // Action Configuration not found
                                CommitResponse r = new CommitResponse();
                                r.setMessage("Cannot load Action configuration for "+actionSequenceId);
                                r.setStatus(Status.ERROR);
                                return r;
                                */
                            }
                            
                        }
                        
                        LOGGER.info("Done from actionsMapping");

                    }
                }
            }
            
            throw new ResourceNotFoundException();

        }
        
        throw new ResourceNotFoundException();
        /*
        // ActionSequence not found
        CommitResponse r = new CommitResponse();
        r.setMessage("Cannot find ActionSequence "+actionSequenceId);
        r.setStatus(Status.ERROR);
        return r;
        */

        ///////////////////////// END TEST  /////
        
        /*
	    if(configurableAction.get(actionSequenceId) != null){
    	    ret =  configurableAction.get(actionSequenceId).getConfiguration();
    	    
    	    if(ret != null){
    	        return ret;
    	    }
	    }
	    
	    for(String ba_idx : configurableAction.keySet()){
	        BaseAction ba = configurableAction.get(ba_idx);
	        if(ba != null && ba instanceof FeatureUpdater){
	            
	            ret = ba.getConfiguration();
	            if(ret != null){
	                
	                LOGGER.info("Retrieved BlockConfiguration of "+ba_idx);

    	            try {
    	                
                        OSDIConfigurationKVP actionConfig = (OSDIConfigurationKVP) depot.loadExistingConfiguration(GEOCOLLECT_SCOPEID, ba.getId());
                        
                        if(ret instanceof FeatureUpdaterConfiguration){
                            
                            FeatureUpdaterConfiguration fconfig = (FeatureUpdaterConfiguration) ret;
                            
                            Map<String,String> rules = new HashMap<String, String>();
                            
                            for(String rule_key : actionConfig.getAllKeys()){
                                
                                if(actionConfig.getValue(rule_key) instanceof String){
                                
                                    rules.put(rule_key, (String) actionConfig.getValue(rule_key));
                                }
                                
                            }
                            
                            fconfig.setRules(rules);
                            return fconfig;
                        }
                        
                        LOGGER.info("weeel done");
                        
                    } catch (OSDIConfigurationException e) {
                        LOGGER.warn("Error Loading configuration", e);
                    }
    	            
    	            return ret;
	            }
            } 
	    }
	    
	    return ret;
	    */
    }
	
	/**
	 * 
	 * @param actionSequenceId
	 * @return
	 */
    @RequestMapping(value = "/{actionsequence}/configuration", method = { RequestMethod.POST })
    public @ResponseBody Object setActionConfiguration(
           @PathVariable("actionsequence") String actionSequenceId,
           @RequestBody JSONObject body){

        LOGGER.info("Received call to set Action Configuration:" + actionSequenceId);
        LOGGER.info(body.toJSONString());
        
        Map<String, Object> rulesObj = (Map<String, Object>) body.get("rules");
       
        if(rulesObj == null){
            // Rules not found
            CommitResponse r = new CommitResponse();
            r.setMessage("Missing \"rules\" object");
            r.setStatus(Status.ERROR);
            return r;
        }
        
        //////////////// BEGIN TEST  /////////////////
        if(actionsMapping != null && actionsMapping.containsKey(actionSequenceId)){
            ActionSequence sequence = actionsMapping.get(actionSequenceId);
            
            for(ActionBlock actionBlock : sequence.getActions()){
                
                if(actionBlock != null && actionBlock instanceof FeatureUpdater){
                    
                    BlockConfiguration targetConfiguration = actionBlock.getConfiguration();
                    if(targetConfiguration != null){
                        
                        LOGGER.info("Retrieved BlockConfiguration of "+actionBlock.getId()+" from actionsMapping");

                        if(targetConfiguration instanceof FeatureUpdaterConfiguration){
                            
                            OSDIConfigurationKVP newConfig = new OSDIConfigurationKVP(GEOCOLLECT_SCOPEID, actionBlock.getId());
                            
                            for(Object s : rulesObj.keySet()){
                                if(s != null){
                                    newConfig.addNew(s.toString(), rulesObj.get(s));
                                }
                            } 
                            
                            try {
                                depot.addNewConfiguration(newConfig, true);
                            } catch (OSDIConfigurationException e) {
                                try {
                                    depot.updateExistingConfiguration(newConfig);
                                } catch (OSDIConfigurationException e1) {
                                    // ignore
                                    LOGGER.debug("Cannot create or update OSDI Configuration");
                                }
                            }
                            
                            CommitResponse r = new CommitResponse();
                            r.setMessage("Updated configuration of "+actionBlock.getId());
                            r.setStatus(Status.SUCCESS);
                            return r;
                        }
                        
                        LOGGER.info("Done from actionsMapping");

                    }
                }
            }
            
            
        }
        // Action not found
        CommitResponse r = new CommitResponse();
        r.setMessage("Cannot find ActionSequence "+actionSequenceId);
        r.setStatus(Status.ERROR);
        return r;

        ///////////////////////// END TEST  /////
        /*
        
        if(configurableAction.get(actionSequenceId) != null){
            ret =  configurableAction.get(actionSequenceId).getConfiguration();
            
            if(ret == null){
                
                for(String ba_idx : configurableAction.keySet()){
                    BaseAction ba = configurableAction.get(ba_idx);
                    if(ba != null && ba instanceof FeatureUpdater){
                        ret = ba.getConfiguration();
                        
                        OSDIConfigurationKVP newConfig = new OSDIConfigurationKVP(GEOCOLLECT_SCOPEID, ba.getId());
                        
                        for(Object s : rulesObj.keySet()){
                            if(s != null){
                                newConfig.addNew(s.toString(), rulesObj.get(s));
                            }
                        } 
                        
                        try {
                            depot.addNewConfiguration(newConfig, true);
                        } catch (OSDIConfigurationException e) {
                            try {
                                depot.updateExistingConfiguration(newConfig);
                            } catch (OSDIConfigurationException e1) {
                                // ignore
                            }
                        }
                        
                        CommitResponse r = new CommitResponse();
                        r.setStatus(Status.SUCCESS);
                        return r;
                        
                    }
                }
                
            }
            
            if(ret != null){
                
                LOGGER.info("Retrieved BlockConfiguration of "+actionSequenceId);
                
                if(ret instanceof FeatureUpdaterConfiguration){
                    
                    LOGGER.info("Got a FeatureUpdaterConfiguration");
                }

                CommitResponse r = new CommitResponse();
                r.setStatus(Status.SUCCESS);
                return r;
            }
            
        }
        
        // Action not found
        CommitResponse r = new CommitResponse();
        r.setStatus(Status.ERROR);
        return r;
        
        */
    }
	
	
	@RequestMapping(value = "/{actionsequence}", method = { RequestMethod.GET,	RequestMethod.POST })
	public @ResponseBody Object performAction(
			@PathVariable("actionsequence") String actionSequenceId,
			@RequestBody JSONObject body){
		/* get user details
		 * UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		*/

		// TODO: action "in carico" : set the corresponding feature "working_user" to the username
		//LOGGER.info(body);
		LOGGER.info("Received call for Action " + actionSequenceId);
		/*
		for(String s : actionsMapping.keySet()){
			LOGGER.info(s);
		}
		*/

		if(actionsMapping.containsKey(actionSequenceId)){
			LOGGER.info("Action Exists");
		
			// store the feature using a chain of Actions
			// create a SimpleFeature
			// Manipulate it (do nothing)
			// Store to the DataStore
			// Send mail
			
			try {
			
				// setup the workflow
				ctx = new WorkflowContext();
				
				LOGGER.info(body.toJSONString());
				
				// add the input
				ctx.addContextElement(INPUT_ID, body.toJSONString());
				
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				LOGGER.info("User : "+username);
				
				ctx.addContextElement(MetadataUpdater.CTX_METADATA_USERNAME, username);
				
				// execute the action sequence
				ActionSequence sequence = actionsMapping.get(actionSequenceId);

				
		        for(ActionBlock actionBlock : sequence.getActions()){
		            
		            if(actionBlock != null && actionBlock instanceof FeatureUpdater){
		                
		                BlockConfiguration ret = actionBlock.getConfiguration();
		                if(ret != null){
		                    
		                    LOGGER.info("Retrieved BlockConfiguration of "+actionBlock.getId());

		                    try {
		                        
		                        OSDIConfigurationKVP actionConfig = (OSDIConfigurationKVP) depot.loadExistingConfiguration(GEOCOLLECT_SCOPEID, actionBlock.getId());
		                        
		                        if(ret instanceof FeatureUpdaterConfiguration){
		                            
		                            FeatureUpdaterConfiguration fconfig = (FeatureUpdaterConfiguration) ret;
		                            
		                            Map<String,String> rules = new HashMap<String, String>();
		                            
		                            for(String rule_key : actionConfig.getAllKeys()){
		                                
		                                if(actionConfig.getValue(rule_key) instanceof String){
		                                
		                                    rules.put(rule_key, (String) actionConfig.getValue(rule_key));
		                                }
		                                
		                            }
		                            
		                            fconfig.setRules(rules);
		                            LOGGER.debug("Rules set");
		                        }
		                        
		                    } catch (OSDIConfigurationException e) {
		                        LOGGER.warn("Error Loading configuration", e);
		                    }

		                }
		            }
		        }

				// Execute the sequence
				sequence.execute(ctx);
				
				if(WorkflowStatus.Status.COMPLETED == ctx.getStatusElements().get(WRITER_ID).getCurrentStatus()){
					
					// SUCCESS
					CommitResponse r = new CommitResponse();
					
					// datastorewriter stores the output feature ids in the "outputid" context element
					Object outputids = ctx.getContextElements().get(OUTPUTIDS);
					if(outputids != null && outputids instanceof List){
						
						List<FeatureId> outList = (List<FeatureId>)outputids;
						if(!outList.isEmpty()){
							// There should be only one ID
							r.setId(outList.get(0).getID());
						}

						r.setStatus(Status.SUCCESS);
						
						return r;
					}
					
				}

				// Failure
				// this will go through and the ERROR to be returned
				
			} catch (WorkflowException wfe) {
			    LOGGER.error("Error executing ActionSequence", wfe);
			}
			
		}else{
			LOGGER.info("Action DOES NOT Exists");
		}
		
		CommitResponse r = new CommitResponse();
		r.setId("-1");
		r.setStatus(Status.ERROR);
		return r;
	}

	public Map<String, ActionSequence> getActionsMapping() {
		return actionsMapping;
	}

	public void setActionsMapping(Map<String, ActionSequence> actionsMapping) {
		this.actionsMapping = actionsMapping;
	}

    @Override
    public String getInstanceID(HttpServletRequest req) {
        return "";
    }
}
