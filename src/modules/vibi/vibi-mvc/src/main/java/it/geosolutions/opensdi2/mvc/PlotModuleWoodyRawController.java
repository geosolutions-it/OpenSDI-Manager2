package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.PlotModuleWoodyRaw;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotModuleWoodyRawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/plotModuleWoodyRaw")
public class PlotModuleWoodyRawController extends BaseController<PlotModuleWoodyRaw, String> {

    @Autowired
    PlotModuleWoodyRawService plotModuleWoodyRawService;

    @Override
    protected BaseService<PlotModuleWoodyRaw, String> getBaseService() {
        return plotModuleWoodyRawService;
    }
}
