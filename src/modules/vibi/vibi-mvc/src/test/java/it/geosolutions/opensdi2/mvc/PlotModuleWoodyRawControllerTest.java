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
    public void testCrud() {
        MvcTestsUtils.create("plotModuleWoodyRaw",
                MvcTestsUtils.readResourceFile("plotModuleWoodyRaw/create_plotModuleWoodyRaw.json"));
        List<PlotModuleWoodyRaw> plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "plotNo:=:'1101';species:=:'Acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(1));
        assertThat(plotModuleWoodyRaw.get(0).getCount(), is("22.0"));
        MvcTestsUtils.update("plotModuleWoodyRaw", plotModuleWoodyRaw.get(0).getFid(),
                MvcTestsUtils.readResourceFile("plotModuleWoodyRaw/update_plotModuleWoodyRaw.json"));
        plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "plotNo:=:'1101';species:=:'Acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(1));
        assertThat(plotModuleWoodyRaw.get(0).getCount(), is("23.0"));
        MvcTestsUtils.delete("plotModuleWoodyRaw", plotModuleWoodyRaw.get(0).getFid());
        plotModuleWoodyRaw = MvcTestsUtils.list(PLOT_MODULE_WOODY_RAW_GENERIC_TYPE, "plotModuleWoodyRaw",
                null, "plotNo:=:'1101';species:=:'Acer pensylvanicum'", null, null, null, null);
        assertThat(plotModuleWoodyRaw.size(), is(0));
    }
}
