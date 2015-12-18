package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbhCm;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotModuleWoodyDbhCmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/plotModuleWoodyDbhCm")
public class PlotModuleWoodyDbhCmController extends BaseController<PlotModuleWoodyDbhCm> {

    @Autowired
    PlotModuleWoodyDbhCmService plotModuleWoodyDbhCmService;

    @Override
    protected BaseService<PlotModuleWoodyDbhCm> getBaseService() {
        return plotModuleWoodyDbhCmService;
    }
}
