package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Metrics;
import it.geosolutions.opensdi2.persistence.Species;
import it.geosolutions.opensdi2.service.BaseService;
import it.geosolutions.opensdi2.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/vibi/metrics")
public class MetricsController extends BaseController<Metrics> {

    @Autowired
    private MetricsService metricsService;

    @Override
    protected BaseService<Metrics> getBaseService() {
        return metricsService;
    }
}
