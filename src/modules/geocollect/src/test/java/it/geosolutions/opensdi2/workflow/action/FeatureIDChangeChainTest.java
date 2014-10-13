package it.geosolutions.opensdi2.workflow.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.ActionSequence;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.WorkflowStatus;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Point;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/geocollect-featureidchange.xml")
public class FeatureIDChangeChainTest {
	
	public static Map<String,Class> GEOJSON_ATTRIBUTES = new HashMap<String, Class>();
	
	static {
		GEOJSON_ATTRIBUTES.put("RESPONSABILE_ABBANDONO", String.class);
		GEOJSON_ATTRIBUTES.put("INDIRIZZO", String.class);
		GEOJSON_ATTRIBUTES.put("TIPOLOGIA_RIFIUTO", String.class);
		GEOJSON_ATTRIBUTES.put("LOCALITA", String.class);
		GEOJSON_ATTRIBUTES.put("RIMOZIONE", String.class);
		GEOJSON_ATTRIBUTES.put("PROVENIENZA_SEGNALAZIONE", String.class);
		GEOJSON_ATTRIBUTES.put("PRESA_IN_CARICO", String.class);
		GEOJSON_ATTRIBUTES.put("COGNOME_RILEVATORE", String.class);
		GEOJSON_ATTRIBUTES.put("CIVICO", String.class);
		GEOJSON_ATTRIBUTES.put("DATA_SCHEDA", String.class);
		GEOJSON_ATTRIBUTES.put("SEQUESTRO", String.class);
		GEOJSON_ATTRIBUTES.put("TIPOLOGIA_SEGNALAZIONE", String.class);
		GEOJSON_ATTRIBUTES.put("DATA_AGG", String.class);
		GEOJSON_ATTRIBUTES.put("EMAIL", String.class);
		GEOJSON_ATTRIBUTES.put("NOME_RILEVATORE", String.class);
		GEOJSON_ATTRIBUTES.put("COMUNE", String.class);
		GEOJSON_ATTRIBUTES.put("QUANTITA_PRESUNTA", String.class);
		GEOJSON_ATTRIBUTES.put("ENTE_RILEVATORE", String.class);
		
		GEOJSON_ATTRIBUTES.put("geometry", Point.class);
	}
	
	private WorkflowContext ctx;
	
	/**
	 * The ActionSequence to execute (to test)
	 */
	@Autowired
	private ActionSequence sequence_wo_email;	
	
	/**
	 * The configuration to use
	 */
	@Autowired
	private DataStoreConfiguration cfg;
	
	private DataStore store;
	
	private static final String INPUT_ID = "input";
	private static final String GEOJSON_RESOURCE = "/geojson_geocollect.json";
	private static final String WRITER_ID = "writer";
	
	@Before
	public void setUp() throws IOException {
		store = cfg.getDataStore();
		TestUtils.addSchemaToStore(store, TestUtils.SAMPLE_FEATURE_NAME, GEOJSON_ATTRIBUTES);
		ctx = new WorkflowContext();
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
	}
	
	/**
	 * Test a chain of Actions to:
	 * 
	 * - Read a GeoJSON
	 * - Manipulate the FeatureID stripping out the prefix, leaving only the ID
	 * - Store the modified Feature
	 * 
	 * @throws WorkflowException
	 * @throws IOException
	 */
	@Test
	public void stripFeatureIDTest() throws WorkflowException, IOException {
		sequence_wo_email.execute(ctx);
		assertTrue(ctx.getStatusElements().containsKey(WRITER_ID));
		
		assertEquals(WorkflowStatus.Status.COMPLETED, 
				ctx.getStatusElements().get(WRITER_ID).getCurrentStatus());
		
		SimpleFeatureSource featureSource = store.getFeatureSource(TestUtils.SAMPLE_FEATURE_NAME);
		FeatureCollection fc = featureSource.getFeatures();
		assertEquals(1, fc.size());
		SimpleFeature readFeature = (SimpleFeature) fc.features().next();
		assertEquals(TestUtils.SAMPLE_FEATURE_NAME, readFeature.getName().getLocalPart());
		
		assertEquals("noto ", readFeature.getAttribute("RESPONSABILE_ABBANDONO"));
		assertEquals("08/ott/2014", readFeature.getAttribute("DATA_AGG"));
		assertTrue(readFeature.getAttribute("geometry") instanceof Point);
	}
}
