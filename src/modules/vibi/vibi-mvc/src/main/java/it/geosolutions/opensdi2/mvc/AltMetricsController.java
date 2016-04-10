package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.persistence.AltMetrics;
import it.geosolutions.opensdi2.service.AltMetricsService;
import it.geosolutions.opensdi2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vibi/altMetrics")
public class AltMetricsController extends BaseController<AltMetrics, Integer> {

    @Autowired
    private AltMetricsService metricsService;

    @Override
    protected BaseService<AltMetrics, Integer> getBaseService() {
        return metricsService;
    }
}
