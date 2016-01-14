package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.PlotService;
import it.geosolutions.opensdi2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/plot")
public class PlotController extends BaseController<Plot> {

    @Autowired
    PlotService plotService;

    @Override
    protected BaseService<Plot> getBaseService() {
        return plotService;
    }
}
