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
        MvcTestsUtils.create("plotModuleHerbaceous",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceous/create_plotModuleHerbaceous.json"));
        List<PlotModuleHerbaceous> plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "plotNo:=:'1101';species:=:'Acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(1));
        assertThat(plotModuleHerbaceous.get(0).getSpecies(), is("Acer pensylvanicum"));
        assertThat(plotModuleHerbaceous.get(0).getDepth(), is(3));
        MvcTestsUtils.update("plotModuleHerbaceous", plotModuleHerbaceous.get(0).getFid(),
                MvcTestsUtils.readResourceFile("plotModuleHerbaceous/update_plotModuleHerbaceous.json"));
        plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "fid:=:'" + plotModuleHerbaceous.get(0).getFid() + "'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(1));
        assertThat(plotModuleHerbaceous.get(0).getSpecies(), is("Acer pensylvanicum"));
        assertThat(plotModuleHerbaceous.get(0).getDepth(), is(4));
        MvcTestsUtils.delete("plotModuleHerbaceous", plotModuleHerbaceous.get(0).getFid());
        plotModuleHerbaceous = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_GENERIC_TYPE, "plotModuleHerbaceous",
                null, "fid:=:'" + plotModuleHerbaceous.get(0).getFid() + "'", null, null, null, null);
        assertThat(plotModuleHerbaceous.size(), is(0));
    }
}
