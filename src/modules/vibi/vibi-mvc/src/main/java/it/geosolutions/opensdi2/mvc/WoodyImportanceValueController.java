package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.PagebleEntityCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.old.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.WoodyImportanceValue;
import it.geosolutions.opensdi2.service.HerbaceousRelativeCoverService;
import it.geosolutions.opensdi2.service.WoodyImportanceValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vibi/woodyImportanceValue")
public class WoodyImportanceValueController extends CRUDControllerBase<WoodyImportanceValue, String>
        implements SimpleEntityCRUDController<WoodyImportanceValue, String>,
        DumpRestoreCRUDController<WoodyImportanceValue> {

    @Autowired
    WoodyImportanceValueService herbaceousRelativeCoverService;

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    CRUDResponseWrapper<WoodyImportanceValue> list() {
        List<WoodyImportanceValue> herbaceousRelativeCovers = herbaceousRelativeCoverService.getAll();
        CRUDResponseWrapper<WoodyImportanceValue> result = new CRUDResponseWrapper<WoodyImportanceValue>();
        result.setCount(herbaceousRelativeCovers.size());
        result.setTotalCount(herbaceousRelativeCovers.size());
        result.setData(herbaceousRelativeCovers);
        return result;
    }

    @Override
    public String create(@RequestBody WoodyImportanceValue c) throws RESTControllerException {
        return null;
    }

    @Override
    public WoodyImportanceValue get(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@PathVariable(value = "id") String id, @RequestBody WoodyImportanceValue c) throws RESTControllerException {
        return null;
    }

    @Override
    public String update(@RequestBody WoodyImportanceValue c) throws RESTControllerException {
        return null;
    }

    @Override
    public String delete(@PathVariable(value = "id") String id) throws RESTControllerException {
        return null;
    }

    @Override
    public CRUDResponseWrapper<WoodyImportanceValue> dump() throws RESTControllerException {
        return null;
    }

    @Override
    public String restore(@RequestBody CRUDResponseWrapper<WoodyImportanceValue> lcd) throws RESTControllerException {
        return null;
    }
}
