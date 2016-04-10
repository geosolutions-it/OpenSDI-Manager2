package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.Fds2SpeciesMiscInfo;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.Fds2SpeciesMiscInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/fds2SpeciesMiscInfo")
public class Fds2SpeciesMiscInfoController extends BaseController<Fds2SpeciesMiscInfo, String> {

    @Autowired
    Fds2SpeciesMiscInfoService fds2SpeciesMiscInfoService;

    @Override
    protected BaseService<Fds2SpeciesMiscInfo, String> getBaseService() {
        return fds2SpeciesMiscInfoService;
    }
}
