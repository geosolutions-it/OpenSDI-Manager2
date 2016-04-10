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
