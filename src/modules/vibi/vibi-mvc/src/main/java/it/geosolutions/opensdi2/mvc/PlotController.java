package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/plot")
public class PlotController extends CRUDControllerBase<Plot, String>
        implements SimpleEntityCRUDController<Plot, String>, DumpRestoreCRUDController<Plot> {

    @Autowired
    PlotService plotService;

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<Plot> list() {
        List<Plot> plots = plotService.getAll();
        CRUDResponseWrapper<Plot> result = new CRUDResponseWrapper<Plot>();
        result.setCount(plots.size());
        result.setTotalCount(plots.size());
        result.setData(plots);
        return result;
    }

    @Override
    public String create(@RequestBody Plot c) throws RESTControllerException {
        return null;
    }

    @Override
    public Plot get(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@PathVariable(value = "id") String id, @RequestBody Plot c) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@RequestBody Plot c) throws RESTControllerException {
        return null;
    }

    @Override
    public String delete(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public CRUDResponseWrapper<Plot> dump() throws RESTControllerException {
        return null;
    }

    @Override
    public String restore(@RequestBody CRUDResponseWrapper<Plot> lcd) throws RESTControllerException {
        return null;
    }
}
