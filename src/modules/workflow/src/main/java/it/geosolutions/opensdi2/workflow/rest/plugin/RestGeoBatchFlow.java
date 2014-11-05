/**
 * 
 */
package it.geosolutions.opensdi2.workflow.rest.plugin;

import it.geosolutions.opensdi2.rest.RestService;
import it.geosolutions.opensdi2.rest.RestServiceRuntime;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author alessio.fabiani
 * 
 */
public class RestGeoBatchFlow extends RestService {

	private List<RestServiceRuntime> runtimes;

	/**
	 * @param serviceId
	 * @param description
	 * @param version
	 * @param activeStatus
	 */
	public RestGeoBatchFlow(String serviceId, String name, String description,
			String version, String activeStatus) {
		super(serviceId, name, description, version, activeStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#getRuntimes()
	 */
	@Override
	public List<RestServiceRuntime> getRuntimes(Principal auth) throws Exception {
		return runtimes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.geosolutions.opensdi2.rest.RestService#execute()
	 */
	@Override
	public String execute(Principal auth, String requestBody, Map<String, String> params) throws Exception {
		runtimes = new ArrayList<RestServiceRuntime>();
		
		runtimes.add(new RestGeoBatchConsumer("65368e58-1133-4bf9-bb43-4b67b5769778", "name_1", "description_1", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("5c9ca5b3-af6d-4a5f-ba73-b495651e2f05", "name_2", "description_2", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("0882324e-0da9-47c4-99df-750193b93764", "name_3", "description_3", "SUCCESS", 100.0f, new Date(), new Date()));
		runtimes.add(new RestGeoBatchConsumer("0c0070f5-7918-4094-904d-f0985a07fc68", "name_4", "description_4", "RUNNING", 37.5f, new Date(), null));
		runtimes.add(new RestGeoBatchConsumer("0882324e-0da9-47c4-99df-750193b93764", "name_5", "description_5", "SUCCESS", 100.0f, new Date(), new Date()));
		
		return "200";
	}

	@Override
	public String stop(RestServiceRuntime runtime, Map<String, String> params) throws Exception {
		if (runtimes != null) {
			if (runtimes.contains(runtime)) {
				runtimes.remove(runtime);
				return "200";		
			}
		}
		
		throw new Exception("Error occurred while stopping service!");
	}

	@Override
	public boolean supportsQueries() {
		return false;
	}

	@Override
	public int countRuntimes() {
		return 0;
	}

	@Override
	public List<RestServiceRuntime> findRuntimes(String id, String status,
			Date startDate, Date endDate, Map<String, String> params, int page,
			int pageSize) {
		return null;
	}

	@Override
	public RestServiceRuntime getRuntime(String id) {
		return null;
	}

}
