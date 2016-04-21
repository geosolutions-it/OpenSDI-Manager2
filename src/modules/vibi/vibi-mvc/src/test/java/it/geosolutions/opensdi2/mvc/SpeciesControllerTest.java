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
import it.geosolutions.opensdi2.persistence.Species;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class SpeciesControllerTest extends BaseMvcTests {

    private static final GenericType<CRUDResponseWrapper<Species>> SPECIES_GENERIC_TYPE = new GenericType<CRUDResponseWrapper<Species>>() {
    };

    @Test
    public void testCrud() {
        MvcTestsUtils.create("species", MvcTestsUtils.readResourceFile("species/create_species.json"));
        List<Species> species = MvcTestsUtils.list(SPECIES_GENERIC_TYPE, "species",
                null, "scientificName:=:'Acer pensylvanicum Test'", null, null, null, null);
        assertThat(species.size(), is(1));
        assertThat(species.get(0).getScientificName(), is("Acer pensylvanicum Test"));
        assertThat(species.get(0).getAuthority(), is("L."));
        assertThat(species.get(0).getOhStatus(), is("native"));
        MvcTestsUtils.update("species", "Acer pensylvanicum Test", MvcTestsUtils.readResourceFile("species/update_species.json"));
        species = MvcTestsUtils.list(SPECIES_GENERIC_TYPE, "species",
                null, "scientificName:=:'Acer pensylvanicum Test'", null, null, null, null);
        assertThat(species.size(), is(1));
        assertThat(species.get(0).getAuthority(), is("L."));
        assertThat(species.get(0).getOhStatus(), is("non-native"));
        MvcTestsUtils.delete("species", "Acer pensylvanicum Test");
        species = MvcTestsUtils.list(SPECIES_GENERIC_TYPE, "species",
                null, "scientificName:=:'Acer pensylvanicum Test'", null, null, null, null);
        assertThat(species.size(), is(0));
    }
}
