package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotModuleHerbaceousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/vibi/plotModuleHerbaceous")
public class PlotModuleHerbaceousController extends BaseController<PlotModuleHerbaceous, String>{

    @Autowired
    PlotModuleHerbaceousService plotModuleHerbaceousService;

    @Override
    protected BaseService<PlotModuleHerbaceous, String> getBaseService() {
        return plotModuleHerbaceousService;
    }
}
