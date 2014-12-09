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


import java.io.IOException;
import it.geosolutions.geocollect.model.http.CommitResponse;
import it.geosolutions.geocollect.model.http.Status;
import it.geosolutions.opensdi2.workflow.ActionSequence;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.WorkflowStatus;
import it.geosolutions.opensdi2.workflow.action.DataStoreConfiguration;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Controller for a GeoCollect Action 
 * 
 * @author Lorenzo Natali (lorenzo.natali at geo-solutions.it)
 * 
 */
@Controller
@RequestMapping("/geocollect/action")
public class GeoCollectActionController {
	
	/**
	 * Context object to store data to
	 */
	private WorkflowContext ctx;

	/**
	 * Action sequence to execute upon receiving a GeoJSON
	 */
	@Autowired
	private ActionSequence chain;	
	
	/**
	 * DataStoreConfiguration to use
	 */
	@Autowired
	private DataStoreConfiguration cfg;
	
	/**
	 * DataStore instance
	 */
	private DataStore store;
	
	/**
	 * Custom IDs for the workflow
	 */
	private static final String INPUT_ID = "input";
	private static final String WRITER_ID = "writer";

	/**
	 * Available actions
	 */
	public static final String STORE_FEATURE_ACTION = "store";

	
	/**
	 * Logger
	 */
	private final static Logger LOGGER = Logger.getLogger(GeoCollectActionController.class);
	
	@RequestMapping(value = "/{action}", method = { RequestMethod.GET,	RequestMethod.POST })
	public @ResponseBody Object performAction(
			@PathVariable("action") String action,
			@RequestBody JSONObject body){
		/* get user details
		 * UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		*/
		
		// TODO: action "in carico" : set the corresponding feature "working_user" to the username
		//
		LOGGER.info(body);
		
		if(STORE_FEATURE_ACTION.equals(action)){
			// store the feature using a chain of Actions
			// create a SimpleFeature
			// Manipulate it (do nothing)
			// Store to the DataStore
			// Send mail
			
			try {
				store = cfg.getDataStore();
			
				// setup the workflow
				ctx = new WorkflowContext();
				
				LOGGER.info(body.toJSONString());
				
				// add the input
				ctx.addContextElement(INPUT_ID, body.toJSONString());
				
				// execute the action sequence
				chain.execute(ctx);
				
				if(WorkflowStatus.Status.COMPLETED == ctx.getStatusElements().get(WRITER_ID).getCurrentStatus()){
					
					// SUCCESS
					CommitResponse r = new CommitResponse();
					
					// TODO: what to set here?
					// datastorewriter does not return anything at the moment
					r.setId("2");
					
					r.setStatus(Status.SUCCESS);
					
					return r;
					
				}else{
					// FAIL!
					
					// this will go through and the ERROR to be returned
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WorkflowException wfe) {
				// TODO Auto-generated catch block
				wfe.printStackTrace();
			}
			
		}
		
		CommitResponse r = new CommitResponse();
		r.setId("2");
		
		r.setStatus(Status.ERROR);
		return r;
	}
}
