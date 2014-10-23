/**
 * 
 */
package it.geosolutions.opensdi2.workflow.rest.plugin;

import java.util.ArrayList;
import java.util.List;

import it.geosolutions.opensdi2.rest.RestItemParameter;
import it.geosolutions.opensdi2.rest.RestPlugin;
import it.geosolutions.opensdi2.rest.RestService;

/**
 * @author alessio.fabiani
 *
 */
public class RestGeoBatchPlugin implements RestPlugin {

	List<RestService> flows = new ArrayList<RestService>();
	
	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return "geobatch";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getDescription()
	 */
	@Override
	public String getDescription() {
		return "GeoBatch REST Plugin for OpenSDI2-Manager V.1.0";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/* (non-Javadoc)
	 * @see it.geosolutions.opensdi2.rest.RestPlugin#getServices()
	 */
	@Override
	public List<RestService> getServices() {
		
		if (flows.isEmpty()) {
			flows.add(new RestGeoBatchFlow("csvfileingestion", "csvfileingestion", "CSV File Ingestion", "1.4-SNAPSHOT", "ENABLED"));

			final RestGeoBatchFlow gtiffFlow = new RestGeoBatchFlow("gtifffileingestion", "gtifffileingestion", "Prepare GeoTIFF and publish", "1.4-SNAPSHOT", "ENABLED");
			flows.add(gtiffFlow);
			List<RestItemParameter> parameters = new ArrayList<RestItemParameter>();
			parameters.add(new RestGeoBatchParameter("testParam", "testValue"));
			gtiffFlow.setParameters(parameters);

			flows.add(new RestGeoBatchFlow("ndvifileingestion", "ndvifileingestion", "NDVI File Ingestion", "1.4-SNAPSHOT", "DISABLED"));
			flows.add(new RestGeoBatchFlow("ndvifilegeneration", "ndvifilegeneration", "NDVI Stats Generation", "1.4-SNAPSHOT", "DISABLED"));
		}
		
		return flows;
	}

}
