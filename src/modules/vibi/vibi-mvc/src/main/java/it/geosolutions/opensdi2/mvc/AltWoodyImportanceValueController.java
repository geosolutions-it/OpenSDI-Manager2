package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.AltWoodyImportanceValue;
import it.geosolutions.opensdi2.service.AltWoodyImportanceValueService;
import it.geosolutions.opensdi2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/altWoodyImportanceValue")
public class AltWoodyImportanceValueController extends BaseController<AltWoodyImportanceValue, Integer> {

    @Autowired
    AltWoodyImportanceValueService herbaceousRelativeCoverService;

    @Override
    protected BaseService<AltWoodyImportanceValue, Integer> getBaseService() {
        return herbaceousRelativeCoverService;
    }
}
