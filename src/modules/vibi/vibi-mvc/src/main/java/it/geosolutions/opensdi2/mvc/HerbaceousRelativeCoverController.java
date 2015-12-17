package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/herbaceousRelativeCover")
public class HerbaceousRelativeCoverController extends BaseController<HerbaceousRelativeCover> {

    @Autowired
    HerbaceousRelativeCoverService herbaceousRelativeCoverService;

    @Override
    protected BaseService<HerbaceousRelativeCover> getBaseService() {
        return herbaceousRelativeCoverService;
    }
}
