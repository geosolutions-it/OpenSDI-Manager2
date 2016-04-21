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
import it.geosolutions.opensdi2.persistence.BiomassRaw;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class BiomassRawControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<BiomassRaw>>
            BIOMASS_RAW_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<BiomassRaw>>() {
    };

    @Test
    public void testCrud() {
        MvcTestsUtils.create("biomassRaw",
                MvcTestsUtils.readResourceFile("biomassRaw/create_biomassRaw.json"));
        List<BiomassRaw> biomassRaw = MvcTestsUtils.list(BIOMASS_RAW_GENERIC_TYPE, "biomassRaw",
                null, "date_time:>:'2016-04-11 15:00:00+00';date_time:<:'2016-04-11 16:00:00+00'", null, null, null, null);
        assertThat(biomassRaw.size(), is(1));
        assertThat(biomassRaw.get(0).getAreaSampled(), is(0.1));
        MvcTestsUtils.update("biomassRaw", biomassRaw.get(0).getFid(),
                MvcTestsUtils.readResourceFile("biomassRaw/update_biomassRaw.json"));
        biomassRaw = MvcTestsUtils.list(BIOMASS_RAW_GENERIC_TYPE, "biomassRaw",
                null, "date_time:>:'2016-04-11 15:00:00+00';date_time:<:'2016-04-11 16:00:00+00'", null, null, null, null);
        assertThat(biomassRaw.size(), is(1));
        assertThat(biomassRaw.get(0).getAreaSampled(), is(0.2));
        MvcTestsUtils.delete("biomassRaw", biomassRaw.get(0).getFid());
        biomassRaw = MvcTestsUtils.list(BIOMASS_RAW_GENERIC_TYPE, "biomassRaw",
                null, "date_time:>:'2016-04-11 15:00:00+00';date_time:<:'2016-04-11 16:00:00+00'", null, null, null, null);
        assertThat(biomassRaw.size(), is(0));
    }
}
