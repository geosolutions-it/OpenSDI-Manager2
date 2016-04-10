package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.LeapLandcoverClassification;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.LeapLandcoverClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/leapLandcoverClassification")
public class LeapLandcoverClassificationController extends BaseController<LeapLandcoverClassification, String> {

    @Autowired
    LeapLandcoverClassificationService leapLandcoverClassificationService;

    @Override
    protected BaseService<LeapLandcoverClassification, String> getBaseService() {
        return leapLandcoverClassificationService;
    }
}
