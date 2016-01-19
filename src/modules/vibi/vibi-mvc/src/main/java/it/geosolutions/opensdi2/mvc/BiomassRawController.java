package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.BiomassRaw;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.BiomassRawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/biomassRaw")
public class BiomassRawController extends BaseController<BiomassRaw, String> {

    @Autowired
    BiomassRawService biomassRawService;

    @Override
    protected BaseService<BiomassRaw, String> getBaseService() {
        return biomassRawService;
    }
}
