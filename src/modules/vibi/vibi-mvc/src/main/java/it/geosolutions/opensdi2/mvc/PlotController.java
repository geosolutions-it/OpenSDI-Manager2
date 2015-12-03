package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/plot")
public class PlotController {

    @Autowired
    PlotService plotService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<Plot> list(@RequestParam(required = false) String keyword,
                                                      @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                                      @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                                      @RequestParam(required = false, defaultValue = "-1") Integer page) {
        List<Plot> plots = plotService.getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<Plot> result = new CRUDResponseWrapper<Plot>();
        result.setCount(plots.size());
        result.setTotalCount(plots.size());
        result.setData(plots);
        return result;
    }
}
