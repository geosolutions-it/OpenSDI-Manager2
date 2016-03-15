package it.geosolutions.opensdi2.mvc;

import com.sun.jersey.api.client.GenericType;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Plot;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class PlotControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<Plot>> PLOT_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<Plot>>() {
    };

    @Test
    public void testCrudPlot() {
        MvcTestsUtils.create("classCodeModNatureServe", MvcTestsUtils.readResourceFile("plot/create_classCodeModNatureServe.json"));
        MvcTestsUtils.create("plot", MvcTestsUtils.readResourceFile("plot/create_plot.json"));
        List<Plot> plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "morgan", "plotNo:=:'1101'", null, null, null, null);
        assertThat(plots.size(), is(1));
        assertThat(plots.get(0).getPlotNo(), is("1101"));
        assertThat(plots.get(0).getPlotName(), is("The Morgan Factor"));
        MvcTestsUtils.update("plot", "1101", MvcTestsUtils.readResourceFile("plot/update_plot.json"));
        plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "morgan", "plotNo:=:'1101'", null, null, null, null);
        assertThat(plots.size(), is(1));
        assertThat(plots.get(0).getPlotNo(), is("1101"));
        assertThat(plots.get(0).getPlotName(), is("The Morgan Factor Updated"));
        MvcTestsUtils.delete("plot", "1101");
        plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "Morgan Factor", "plotNo:=:'1101'", null, null, null, null);
        assertThat(plots.size(), is(0));
        MvcTestsUtils.delete("classCodeModNatureServe", "W02c");
    }
}
