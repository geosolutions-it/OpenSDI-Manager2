package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Species;
import it.geosolutions.opensdi2.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/vibi/species")
public class SpeciesController {

    @Autowired
    SpeciesService speciesService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<Species> list(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                      @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                      @RequestParam(required = false, defaultValue = "-1") Integer page) {
        List<Species> species = speciesService.getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<Species> result = new CRUDResponseWrapper<Species>();
        result.setCount(species.size());
        result.setTotalCount(species.size());
        result.setData(species);
        return result;
    }
}
