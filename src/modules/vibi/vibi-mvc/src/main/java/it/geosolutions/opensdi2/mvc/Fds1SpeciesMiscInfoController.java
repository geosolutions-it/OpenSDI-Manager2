package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.Fds1SpeciesMiscInfo;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.Fds1SpeciesMiscInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/fds1SpeciesMiscInfo")
public class Fds1SpeciesMiscInfoController extends BaseController<Fds1SpeciesMiscInfo, String> {

    @Autowired
    Fds1SpeciesMiscInfoService fds1SpeciesMiscInfoService;

    @Override
    protected BaseService<Fds1SpeciesMiscInfo, String> getBaseService() {
        return fds1SpeciesMiscInfoService;
    }
}
