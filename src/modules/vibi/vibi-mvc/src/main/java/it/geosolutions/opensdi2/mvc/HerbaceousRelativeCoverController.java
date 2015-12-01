package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.PagebleEntityCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/herbaceousRelativeCover")
public class HerbaceousRelativeCoverController extends CRUDControllerBase<HerbaceousRelativeCover, String>
        implements SimpleEntityCRUDController<HerbaceousRelativeCover, String>,
        DumpRestoreCRUDController<HerbaceousRelativeCover> {

    @Autowired
    HerbaceousRelativeCoverService herbaceousRelativeCoverService;

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<HerbaceousRelativeCover> list() {
        List<HerbaceousRelativeCover> herbaceousRelativeCovers = herbaceousRelativeCoverService.getAll();
        CRUDResponseWrapper<HerbaceousRelativeCover> result = new CRUDResponseWrapper<HerbaceousRelativeCover>();
        result.setCount(herbaceousRelativeCovers.size());
        result.setTotalCount(herbaceousRelativeCovers.size());
        result.setData(herbaceousRelativeCovers);
        return result;
    }

    @Override
    public String create(@RequestBody HerbaceousRelativeCover c) throws RESTControllerException {
        return null;
    }

    @Override
    public HerbaceousRelativeCover get(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@PathVariable(value = "id") String id, @RequestBody HerbaceousRelativeCover c) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@RequestBody HerbaceousRelativeCover c) throws RESTControllerException {
        return null;
    }

    @Override
    public String delete(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public CRUDResponseWrapper<HerbaceousRelativeCover> dump() throws RESTControllerException {
        return null;
    }

    @Override
    public String restore(@RequestBody CRUDResponseWrapper<HerbaceousRelativeCover> lcd) throws RESTControllerException {
        return null;
    }
}
