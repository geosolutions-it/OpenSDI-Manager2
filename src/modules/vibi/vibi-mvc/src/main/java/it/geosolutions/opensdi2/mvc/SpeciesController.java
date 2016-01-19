package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.Species;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/species")
public class SpeciesController extends BaseController<Species, String> {

    @Autowired
    SpeciesService speciesService;

    @Override
    protected BaseService<Species, String> getBaseService() {
        return speciesService;
    }
}
