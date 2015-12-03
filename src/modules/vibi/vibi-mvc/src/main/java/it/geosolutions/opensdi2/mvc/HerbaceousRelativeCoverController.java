package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/herbaceousRelativeCover")
public class HerbaceousRelativeCoverController {

    @Autowired
    HerbaceousRelativeCoverService herbaceousRelativeCoverService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<HerbaceousRelativeCover> list(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                                      @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                                      @RequestParam(required = false, defaultValue = "-1") Integer page) {
        List<HerbaceousRelativeCover> herbaceousRelativeCovers = herbaceousRelativeCoverService.
                getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<HerbaceousRelativeCover> result = new CRUDResponseWrapper<HerbaceousRelativeCover>();
        result.setCount(herbaceousRelativeCovers.size());
        result.setTotalCount(herbaceousRelativeCovers.size());
        result.setData(herbaceousRelativeCovers);
        return result;
    }
}
