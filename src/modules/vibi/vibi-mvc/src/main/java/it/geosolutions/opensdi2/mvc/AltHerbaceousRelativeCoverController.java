package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.AltHerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.AltHerbaceousRelativeCoverService;
import it.geosolutions.opensdi2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/altHerbaceousRelativeCover")
public class AltHerbaceousRelativeCoverController extends BaseController<AltHerbaceousRelativeCover, Integer> {

    @Autowired
    AltHerbaceousRelativeCoverService herbaceousRelativeCoverService;

    @Override
    protected BaseService<AltHerbaceousRelativeCover, Integer> getBaseService() {
        return herbaceousRelativeCoverService;
    }
}
