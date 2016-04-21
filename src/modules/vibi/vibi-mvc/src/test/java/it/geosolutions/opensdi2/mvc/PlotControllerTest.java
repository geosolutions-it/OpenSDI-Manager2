/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
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
    public void testCrud() {
        MvcTestsUtils.create("plot", MvcTestsUtils.readResourceFile("plot/create_plot.json"));
        List<Plot> plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "morgan", "plotNo:=:'TEST_1101'", null, null, null, null);
        assertThat(plots.size(), is(1));
        assertThat(plots.get(0).getPlotNo(), is("TEST_1101"));
        assertThat(plots.get(0).getPlotName(), is("The Morgan Factor"));
        MvcTestsUtils.update("plot", "TEST_1101", MvcTestsUtils.readResourceFile("plot/update_plot.json"));
        plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "morgan", "plotNo:=:'TEST_1101'", null, null, null, null);
        assertThat(plots.size(), is(1));
        assertThat(plots.get(0).getPlotNo(), is("TEST_1101"));
        assertThat(plots.get(0).getPlotName(), is("The Morgan Factor Updated"));
        MvcTestsUtils.delete("plot", "TEST_1101");
        plots = MvcTestsUtils.list(PLOT_GENERIC_TYPE, "plot", "Morgan Factor", "plotNo:=:'TEST_1101'", null, null, null, null);
        assertThat(plots.size(), is(0));
    }
}
