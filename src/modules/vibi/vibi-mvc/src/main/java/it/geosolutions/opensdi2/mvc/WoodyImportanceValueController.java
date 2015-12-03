package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.WoodyImportanceValue;
import it.geosolutions.opensdi2.service.WoodyImportanceValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/vibi/woodyImportanceValue")
public class WoodyImportanceValueController {

    @Autowired
    WoodyImportanceValueService herbaceousRelativeCoverService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<WoodyImportanceValue> list(@RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false, defaultValue = "50") Integer maxResults,
                                                   @RequestParam(required = false, defaultValue = "-1") Integer firstResult,
                                                   @RequestParam(required = false, defaultValue = "-1") Integer page) {
        List<WoodyImportanceValue> herbaceousRelativeCovers = herbaceousRelativeCoverService.
                getAll(keyword, null, null, maxResults, firstResult, page);
        CRUDResponseWrapper<WoodyImportanceValue> result = new CRUDResponseWrapper<WoodyImportanceValue>();
        result.setCount(herbaceousRelativeCovers.size());
        result.setTotalCount(herbaceousRelativeCovers.size());
        result.setData(herbaceousRelativeCovers);
        return result;
    }
}
