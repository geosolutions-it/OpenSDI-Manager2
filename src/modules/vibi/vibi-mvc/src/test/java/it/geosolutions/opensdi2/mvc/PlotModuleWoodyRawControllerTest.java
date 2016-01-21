package it.geosolutions.opensdi2.mvc;

import com.sun.jersey.api.client.GenericType;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyRaw;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class PlotModuleWoodyRawControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<PlotModuleWoodyRaw>>
            PLOT_MODULE_WOODY_RAW_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<PlotModuleWoodyRaw>>() {
    };

    @Test
    public void testCrudSpecies() {
        MvcTestsUtils.create("classCodeModNatureServe", MvcTestsUtils.readResourceFile("plot/create_classCodeModNatureServe.json"));
        MvcTestsUtils.create("plot", MvcTestsUtils.readResourceFile("plot/create_plot.json"));
        MvcTestsUtils.create("species", MvcTestsUtils.readResourceFile("species/create_species.json"));
        MvcTestsUtils.create("plotModuleWoodyRaw",
                MvcTestsUtils.readResourceFile("plotModuleWoodyRaw/create_plotModuleWoodyRaw.json"));
        List<PlotModuleWoodyRaw> plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "fid:=:'1101-2-Acalypha deamii-clump'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(1));
        assertThat(plotModuleWoodyRaw.get(0).getFid(), is("1101-2-Acalypha deamii-clump"));
        assertThat(plotModuleWoodyRaw.get(0).getCount(), is("22.0"));
        MvcTestsUtils.update("plotModuleWoodyRaw", "1101-2-Acalypha deamii-clump",
                MvcTestsUtils.readResourceFile("plotModuleWoodyRaw/update_plotModuleWoodyRaw.json"));
        plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "fid:=:'1101-2-Acalypha deamii-clump'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(1));
        assertThat(plotModuleWoodyRaw.get(0).getFid(), is("1101-2-Acalypha deamii-clump"));
        assertThat(plotModuleWoodyRaw.get(0).getCount(), is("23.0"));
        MvcTestsUtils.delete("plotModuleWoodyRaw", "1101-2-Acalypha deamii-clump");
        plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "fid:=:'1101-2-Acalypha deamii-clump'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(0));
        MvcTestsUtils.delete("plot", "1101");
        MvcTestsUtils.delete("species", "Acalypha deamii");
        MvcTestsUtils.delete("classCodeModNatureServe", "W02c");
    }
}
