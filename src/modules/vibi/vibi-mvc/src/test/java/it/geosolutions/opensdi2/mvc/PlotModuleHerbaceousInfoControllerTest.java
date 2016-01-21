package it.geosolutions.opensdi2.mvc;

import com.sun.jersey.api.client.GenericType;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousInfo;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class PlotModuleHerbaceousInfoControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<PlotModuleHerbaceousInfo>>
            PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<PlotModuleHerbaceousInfo>>() {
    };

    @Test
    public void testCrudSpecies() {
        MvcTestsUtils.create("classCodeModNatureServe", MvcTestsUtils.readResourceFile("plot/create_classCodeModNatureServe.json"));
        MvcTestsUtils.create("plot", MvcTestsUtils.readResourceFile("plot/create_plot.json"));
        MvcTestsUtils.create("coverMidpointLookup",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceousInfo/create_coverClassMidpoint.json"));
        MvcTestsUtils.create("plotModuleHerbaceousInfo",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceousInfo/create_plotModuleHerbaceousInfo.json"));
        List<PlotModuleHerbaceousInfo> plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "fid:=:'1101-2-2-%open water'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(1));
        assertThat(plotModuleHerbaceousInfo.get(0).getFid(), is("1101-2-2-%open water"));
        assertThat(plotModuleHerbaceousInfo.get(0).getDepth(), is(0));
        MvcTestsUtils.update("plotModuleHerbaceousInfo", "1101-2-2-%open water",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceousInfo/update_plotModuleHerbaceousInfo.json"));
        plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "fid:=:'1101-2-2-%open water'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(1));
        assertThat(plotModuleHerbaceousInfo.get(0).getFid(), is("1101-2-2-%open water"));
        assertThat(plotModuleHerbaceousInfo.get(0).getDepth(), is(1));
        MvcTestsUtils.delete("plotModuleHerbaceousInfo", "1101-2-2-%open water");
        plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "fid:=:'1101-2-2-%open water'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(0));
        MvcTestsUtils.delete("plot", "1101");
        MvcTestsUtils.delete("classCodeModNatureServe", "W02c");
        MvcTestsUtils.delete("coverMidpointLookup", "0");
    }
}
