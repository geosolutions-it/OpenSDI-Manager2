package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.Biomass;
import it.geosolutions.opensdi2.persistence.BiomassRaw;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.BiomassRawService;
import it.geosolutions.opensdi2.service.BiomassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/biomass")
public class BiomassController extends BaseController<Biomass, String> {

    @Autowired
    BiomassService biomassService;

    @Override
    protected BaseService<Biomass, String> getBaseService() {
        return biomassService;
    }
}
