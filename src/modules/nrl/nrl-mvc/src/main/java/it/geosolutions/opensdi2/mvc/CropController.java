package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi.dto.RESTCropDescriptor;
import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.service.CropDescriptorService;
import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.utils.ResponseConstants;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author Lorenzo Natali, GeoSolutions REST controller for
 *         <RESTCropDescriptor>.
 * 
 */
@Controller
@RequestMapping("/cip/crops")
public class CropController extends
		CRUDControllerBase<RESTCropDescriptor, String> implements
		SimpleEntityCRUDController<RESTCropDescriptor, String>,
		DumpRestoreCRUDController<RESTCropDescriptor> {
	@Autowired
	CropDescriptorService cropDescriptorService;

	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<RESTCropDescriptor> list() {
		List<CropDescriptor> cds = cropDescriptorService.getAll();
		CRUDResponseWrapper<RESTCropDescriptor> ret = new CRUDResponseWrapper<RESTCropDescriptor>();
		List<RESTCropDescriptor> retdata = new ArrayList<RESTCropDescriptor>();
		for (CropDescriptor c : cds) {
			retdata.add(new RESTCropDescriptor(c));
		}
		ret.setCount(cds.size());
		ret.setTotalCount(cds.size());
		ret.setData(retdata);
		return ret;

	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody
	String create(@RequestBody RESTCropDescriptor c) {
		cropDescriptorService.persist(c.toCropDescriptor());
		return c.getId();

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	RESTCropDescriptor get(@PathVariable(value = "id") String id) {
		return new RESTCropDescriptor(cropDescriptorService.get(id));

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@PathVariable(value = "id") String id,
			@RequestBody RESTCropDescriptor c) throws RESTControllerException {
		CropDescriptor cc = cropDescriptorService.get(id);
		if (cc != null) {
			cropDescriptorService.update(c.toCropDescriptor());
		} else {
			throw new RESTControllerException("Unable to find crop "
					+ c.getId() + " to update");
		}
		return c.getId();

	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@RequestBody RESTCropDescriptor c)
			throws RESTControllerException {
		CropDescriptor cc = cropDescriptorService.get(c.getId());
		if (cc != null) {
			cropDescriptorService.update(c.toCropDescriptor());
		} else {
			throw new RESTControllerException("Unable to find crop "
					+ c.getId() + " to update");
		}
		return new RESTCropDescriptor(cc).getId();

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	String delete(@PathVariable(value = "id") String id)
			throws RESTControllerException {
		CropDescriptor cc = cropDescriptorService.get(id);
		if (cc != null) {
			cropDescriptorService.delete(id);
		} else {
			throw new RESTControllerException("Can not find resource :" + id);
		}
		return ResponseConstants.SUCCESS;
	}

	@Override
	@RequestMapping(value = "/dump", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<RESTCropDescriptor> dump() {
		return list();

	}

	@Override
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody
	String restore(@RequestBody CRUDResponseWrapper<RESTCropDescriptor> lcd) {
		if (lcd.getData() == null)
			return ResponseConstants.FAILURE;
		for (RESTCropDescriptor c : lcd.getData()) {
			create(c);
		}
		return ResponseConstants.SUCCESS;
	}

}
