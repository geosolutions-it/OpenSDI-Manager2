package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi.model.AgrometDescriptor;
import it.geosolutions.opensdi.service.AgrometDescriptorService;
import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.SimpleEntityCRUDController;
import it.geosolutions.opensdi2.exceptions.RESTControllerException;
import it.geosolutions.opensdi2.utils.ResponseConstants;

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
 * @author Lorenzo Natali, GeoSolutions REST controller for <AgrometDescriptor>.
 * 
 */
@Controller
@RequestMapping("/cip/agromet")
public class AgrometController extends
		CRUDControllerBase<AgrometDescriptor, String> implements
		SimpleEntityCRUDController<AgrometDescriptor, String>,
		DumpRestoreCRUDController<AgrometDescriptor> {
	@Autowired
	AgrometDescriptorService agromtDescriptorService;

	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<AgrometDescriptor> list() {
		List<AgrometDescriptor> cds = agromtDescriptorService.getAll();
		CRUDResponseWrapper<AgrometDescriptor> ret = new CRUDResponseWrapper<AgrometDescriptor>();
		ret.setCount(cds.size());
		ret.setTotalCount(cds.size());
		ret.setData(cds);
		return ret;

	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody
	String create(@RequestBody AgrometDescriptor c) {
		agromtDescriptorService.persist(c);
		return c.getFactor();

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	AgrometDescriptor get(@PathVariable(value = "id") String id) {
		return agromtDescriptorService.get(id);

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@PathVariable(value = "id") String id,
			@RequestBody AgrometDescriptor c) throws RESTControllerException {
		AgrometDescriptor cc = agromtDescriptorService.get(id);
		if (cc != null) {
			agromtDescriptorService.update(c);
		} else {
			throw new RESTControllerException("Unable to find the resource:"
					+ id);
		}
		return c.getFactor();

	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@RequestBody AgrometDescriptor c)
			throws RESTControllerException {
		AgrometDescriptor cc = agromtDescriptorService.get(c.getFactor());
		if (cc != null) {
			agromtDescriptorService.update(c);
			return c.getFactor();
		} else {
			throw new RESTControllerException("Unable to find factor "
					+ c.getFactor() + "");
		}

	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	String delete(@PathVariable(value = "id") String id) {
		agromtDescriptorService.delete(id);
		return ResponseConstants.SUCCESS;
	}

	@Override
	@RequestMapping(value = "/dump", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<AgrometDescriptor> dump() {
		return list();

	}

	@Override
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody
	String restore(@RequestBody CRUDResponseWrapper<AgrometDescriptor> lcd) {
		if (lcd.getData() == null)
			return ResponseConstants.FAILURE;
		for (AgrometDescriptor c : lcd.getData()) {
			create(c);
		}
		return ResponseConstants.SUCCESS;
	}

}
