package it.geosolutions.opensdi2.mvc;

import com.sun.jersey.api.client.GenericType;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class PlotModuleHerbaceousControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<PlotModuleHerbaceous>>
            PLOT_MODULE_HERBACEOUS_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<PlotModuleHerbaceous>>() {
    };

    @Test
    public void testCrud() {
        MvcTestsUtils.create("classCodeModNatureServe", MvcTestsUtils.readResourceFile("plot/create_classCodeModNatureServe.json"));
        MvcTestsUtils.create("plot", MvcTestsUtils.readResourceFile("plot/create_plot.json"));
        MvcTestsUtils.create("species", MvcTestsUtils.readResourceFile("species/create_species.json"));
        MvcTestsUtils.create("coverMidpointLookup",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceous/create_coverClassMidpoint.json"));
        MvcTestsUtils.create("plotModuleHerbaceous",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceous/create_plotModuleHerbaceous.json"));
        List<PlotModuleHerbaceous> plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "fid:=:'1101-1-2-acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(1));
        assertThat(plotModuleHerbaceous.get(0).getFid(), is("1101-1-2-acer pensylvanicum"));
        assertThat(plotModuleHerbaceous.get(0).getDepth(), is(3));
        MvcTestsUtils.update("plotModuleHerbaceous", "1101-1-2-acer pensylvanicum",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceous/update_plotModuleHerbaceous.json"));
        plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "fid:=:'1101-1-2-acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(1));
        assertThat(plotModuleHerbaceous.get(0).getFid(), is("1101-1-2-acer pensylvanicum"));
        assertThat(plotModuleHerbaceous.get(0).getDepth(), is(4));
        MvcTestsUtils.delete("plotModuleHerbaceous", "1101-1-2-acer pensylvanicum");
        plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "fid:=:'1101-1-2-acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(0));
        MvcTestsUtils.delete("plot", "1101");
        MvcTestsUtils.delete("species", "Acer pensylvanicum");
        MvcTestsUtils.delete("classCodeModNatureServe", "W02c");
        MvcTestsUtils.delete("coverMidpointLookup", "0");
    }
}
