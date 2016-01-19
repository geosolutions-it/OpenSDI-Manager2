package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.PlotModuleWoodyDbh;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyRaw;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotModuleWoodyDbhService;
import it.geosolutions.opensdi2.service.PlotModuleWoodyRawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/plotModuleWoodyDbh")
public class PlotModuleWoodyDbhController extends BaseController<PlotModuleWoodyDbh, Integer> {

    @Autowired
    PlotModuleWoodyDbhService plotModuleWoodyDbhService;

    @Override
    protected BaseService<PlotModuleWoodyDbh, Integer> getBaseService() {
        return plotModuleWoodyDbhService;
    }
}
