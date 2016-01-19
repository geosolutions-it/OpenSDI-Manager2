package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousInfo;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotModuleHerbaceousInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/plotModuleHerbaceousInfo")
public class PlotModuleHerbaceousInfoController extends BaseController<PlotModuleHerbaceousInfo, String> {

    @Autowired
    PlotModuleHerbaceousInfoService plotModuleHerbaceousInfoService;

    @Override
    protected BaseService<PlotModuleHerbaceousInfo, String> getBaseService() {
        return plotModuleHerbaceousInfoService;
    }
}
