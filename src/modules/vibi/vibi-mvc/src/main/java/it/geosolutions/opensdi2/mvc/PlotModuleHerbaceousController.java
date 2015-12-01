package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.service.PlotModuleHerbaceousService;
import it.geosolutions.opensdi2.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/plotModuleHerbaceous")
public class PlotModuleHerbaceousController extends CRUDControllerBase<PlotModuleHerbaceous, String>
        implements SimpleEntityCRUDController<PlotModuleHerbaceous, String>, DumpRestoreCRUDController<PlotModuleHerbaceous> {

    @Autowired
    PlotModuleHerbaceousService plotModuleHerbaceousService;

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<PlotModuleHerbaceous> list() {
        List<PlotModuleHerbaceous> plotModuleHerbaceouses = plotModuleHerbaceousService.getAll();
        CRUDResponseWrapper<PlotModuleHerbaceous> result = new CRUDResponseWrapper<PlotModuleHerbaceous>();
        result.setCount(plotModuleHerbaceouses.size());
        result.setTotalCount(plotModuleHerbaceouses.size());
        result.setData(plotModuleHerbaceouses);
        return result;
    }

    @Override
    public String create(@RequestBody PlotModuleHerbaceous c) throws RESTControllerException {
        return null;
    }

    @Override
    public PlotModuleHerbaceous get(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@PathVariable(value = "id") String id, @RequestBody PlotModuleHerbaceous c) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@RequestBody PlotModuleHerbaceous c) throws RESTControllerException {
        return null;
    }

    @Override
    public String delete(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public CRUDResponseWrapper<PlotModuleHerbaceous> dump() throws RESTControllerException {
        return null;
    }

    @Override
    public String restore(@RequestBody CRUDResponseWrapper<PlotModuleHerbaceous> lcd) throws RESTControllerException {
        return null;
    }
}
