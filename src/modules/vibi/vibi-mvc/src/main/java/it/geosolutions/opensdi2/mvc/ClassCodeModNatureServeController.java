package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.ClassCodeModNatureServe;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.ClassCodeModNatureServeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/classCodeModNatureServe")
public class ClassCodeModNatureServeController extends BaseController<ClassCodeModNatureServe, String> {

    @Autowired
    ClassCodeModNatureServeService classCodeModNatureServeService;

    @Override
    protected BaseService<ClassCodeModNatureServe, String> getBaseService() {
        return classCodeModNatureServeService;
    }
}
