package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/plot")
public class PlotController extends BaseController<Plot, String> {

    @Autowired
    PlotService plotService;

    @Override
    protected BaseService<Plot, String> getBaseService() {
        return plotService;
    }
}
