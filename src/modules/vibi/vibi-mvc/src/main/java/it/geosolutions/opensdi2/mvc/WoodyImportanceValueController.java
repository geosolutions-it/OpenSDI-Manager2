package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.WoodyImportanceValue;
import it.geosolutions.opensdi2.service.BaseService;
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
public class WoodyImportanceValueController extends BaseController<WoodyImportanceValue, Integer> {

    @Autowired
    WoodyImportanceValueService herbaceousRelativeCoverService;

    @Override
    protected BaseService<WoodyImportanceValue, Integer> getBaseService() {
        return herbaceousRelativeCoverService;
    }
}
