package it.geosolutions.opensdi2.workflow.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.workflow.ActionSequence;
import it.geosolutions.opensdi2.workflow.WorkflowContext;
import it.geosolutions.opensdi2.workflow.WorkflowException;
import it.geosolutions.opensdi2.workflow.WorkflowStatus;
import it.geosolutions.opensdi2.workflow.utils.TestUtils;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Point;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/geocollect-test.xml")
public class ChainTest {
	
	private WorkflowContext ctx;
	
	@Autowired
	private ActionSequence chain;	
	
	@Autowired
	private DataStoreConfiguration cfg;
	
	private DataStore store;
	
	private static final String INPUT_ID = "input";
	private static final String GEOJSON_RESOURCE = "/geojson3.json";
	private static final String WRITER_ID = "writer";
	
	@Before
	public void setUp() throws IOException {
		store = cfg.getDataStore();
		TestUtils.addSchemaToStore(store, TestUtils.SAMPLE_FEATURE_NAME, TestUtils.SAMPLE_ATTRIBUTES);
		ctx = new WorkflowContext();
		ctx.addContextElement(INPUT_ID, TestUtils.readResource(GEOJSON_RESOURCE));
	}
	
	@Test
	public void simpleTest() throws WorkflowException, IOException {
		chain.execute(ctx);
		assertTrue(ctx.getStatusElements().containsKey(WRITER_ID));
		
		assertEquals(WorkflowStatus.Status.COMPLETED, 
				ctx.getStatusElements().get(WRITER_ID).getCurrentStatus());
		
		SimpleFeatureSource featureSource = store.getFeatureSource(TestUtils.SAMPLE_FEATURE_NAME);
		FeatureCollection fc = featureSource.getFeatures();
		assertEquals(1, fc.size());
		SimpleFeature readFeature = (SimpleFeature) fc.features().next();
		assertEquals(TestUtils.SAMPLE_FEATURE_NAME, readFeature.getName().getLocalPart());
		assertEquals("value0", readFeature.getAttribute("prop0"));
		assertEquals(100.0, readFeature.getAttribute("prop1"));
		assertTrue(readFeature.getAttribute("geometry") instanceof Point);
	}
}
