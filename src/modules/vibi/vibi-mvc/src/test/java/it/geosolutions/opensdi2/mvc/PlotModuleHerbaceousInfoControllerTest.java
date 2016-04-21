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
    public void testCrud() {
        MvcTestsUtils.create("plotModuleHerbaceousInfo",
                MvcTestsUtils.readResourceFile("plotModuleHerbaceousInfo/create_plotModuleHerbaceousInfo.json"));
        List<PlotModuleHerbaceousInfo> plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "plotNo:=:'1101';info:=:'info-test'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(1));
        assertThat(plotModuleHerbaceousInfo.get(0).getDepth(), is(0));
        MvcTestsUtils.update("plotModuleHerbaceousInfo", plotModuleHerbaceousInfo.get(0).getFid(),
                MvcTestsUtils.readResourceFile("plotModuleHerbaceousInfo/update_plotModuleHerbaceousInfo.json"));
        plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "plotNo:=:'1101';info:=:'info-test'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(1));
        assertThat(plotModuleHerbaceousInfo.get(0).getDepth(), is(1));
        MvcTestsUtils.delete("plotModuleHerbaceousInfo", plotModuleHerbaceousInfo.get(0).getFid());
        plotModuleHerbaceousInfo = MvcTestsUtils.list(PLOT_MODULE_HERBACEOUS_INFO_GENERIC_TYPE, "plotModuleHerbaceousInfo",
                null, "plotNo:=:'1101';info:=:'info-test'", null, null, null, null);
        assertThat(plotModuleHerbaceousInfo.size(), is(0));
    }
}
