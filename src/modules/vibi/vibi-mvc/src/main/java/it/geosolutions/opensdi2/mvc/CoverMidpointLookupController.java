package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.CoverMidpointLookup;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.CoverMidpointLookupService;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/coverMidpointLookup")
public class CoverMidpointLookupController extends BaseController<CoverMidpointLookup, Integer> {

    @Autowired
    CoverMidpointLookupService coverMidpointLookupService;

    @Override
    protected BaseService<CoverMidpointLookup, Integer> getBaseService() {
        return coverMidpointLookupService;
    }
}
