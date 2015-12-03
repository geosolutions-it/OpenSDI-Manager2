package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
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
public class PlotModuleHerbaceousController {

    @Autowired
    PlotModuleHerbaceousService plotModuleHerbaceousService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<PlotModuleHerbaceous> list(@RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                                   @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                                   @RequestParam(required = false, defaultValue = "-1") Integer page) {
        List<PlotModuleHerbaceous> plotModuleHerbaceouses = plotModuleHerbaceousService.
                getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<PlotModuleHerbaceous> result = new CRUDResponseWrapper<PlotModuleHerbaceous>();
        result.setCount(plotModuleHerbaceouses.size());
        result.setTotalCount(plotModuleHerbaceouses.size());
        result.setData(plotModuleHerbaceouses);
        return result;
    }
}
