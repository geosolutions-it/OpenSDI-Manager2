package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi.dto.CRUDResponseWrapper;
import it.geosolutions.opensdi.model.UnitOfMeasure;
import it.geosolutions.opensdi.service.UnitOfMeasureService;
import it.geosolutions.opensdi2.crud.api.CRUDControllerBase;
import it.geosolutions.opensdi2.crud.api.DumpRestoreCRUDController;
import it.geosolutions.opensdi2.crud.api.QuerableCRUDController;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author Lorenzo Natali, GeoSolutions REST controller for <UnitOfMeasure>
 *         database. Provides basic CRUD operations for the <UnitOfMeasure>
 *         Entity
 * 
 */
@Controller
@RequestMapping("/cip/uom")
public class UnitOfMeasureController extends
		CRUDControllerBase<UnitOfMeasure, String> implements
		SimpleEntityCRUDController<UnitOfMeasure, String>,
		DumpRestoreCRUDController<UnitOfMeasure>,
		QuerableCRUDController<UnitOfMeasure, String>{
	@Autowired
	UnitOfMeasureService unitOfMeasureService;

	/**
	 * List all uoms
	 * 
	 * @return
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<UnitOfMeasure> list() {
		List<UnitOfMeasure> cds = unitOfMeasureService.getAll();
		CRUDResponseWrapper<UnitOfMeasure> ret = new CRUDResponseWrapper<UnitOfMeasure>();
		ret.setCount(cds.size());
		ret.setTotalCount(cds.size());
		ret.setData(cds);
		return ret;

	}

	/**
	 * Createuom
	 * 
	 * @return
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody
	String create(@RequestBody UnitOfMeasure c) {
		unitOfMeasureService.persist(c);
		return c.getId();

	}

	/**
	 * Get uom
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	UnitOfMeasure get(@PathVariable(value = "id") String id) {
		return unitOfMeasureService.get(id);

	}

	/**
	 * Update uom for the id in the path
	 * 
	 * @param id
	 *            the id
	 * @param c
	 *            the new uom
	 * @return
	 * @throws Exception
	 *             error
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@PathVariable(value = "id") String id,
			@RequestBody UnitOfMeasure c) throws RESTControllerException {
		UnitOfMeasure cc = unitOfMeasureService.get(id);
		if (cc != null) {
			unitOfMeasureService.update(c);
		} else {
			throw new RESTControllerException("Unable to find uom " + c.getId()
					+ " to update");
		}
		return c.getId();

	}

	/**
	 * Update uom. Get the id from the body.
	 * 
	 * @param c
	 *            the object by id.
	 * @return
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody
	String update(@RequestBody UnitOfMeasure c) {
		UnitOfMeasure cc = unitOfMeasureService.get(c.getId());
		if (cc != null) {
			unitOfMeasureService.update(c);
		}
		return c.getId();

	}

	/**
	 * Delete Uom
	 * 
	 * @param id
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	String delete(@PathVariable(value = "id") String id) {
		unitOfMeasureService.delete(id);
		return ResponseConstants.SUCCESS;
	}

	/**
	 * Delete Uom in the body
	 * 
	 * @param id
	 */
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public @ResponseBody
	UnitOfMeasure delete(@RequestBody UnitOfMeasure c) {
		unitOfMeasureService.delete(c.getId());
		return c;
	}

	/**
	 * Create a dump of all the Uom
	 * 
	 * @return a list of all the crops
	 */
	@Override
	@RequestMapping(value = "/dump", method = RequestMethod.GET)
	public @ResponseBody
	CRUDResponseWrapper<UnitOfMeasure> dump() {
		return list();

	}

	@Override
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public @ResponseBody
	String restore(@RequestBody CRUDResponseWrapper<UnitOfMeasure> lcd) {
		if (lcd.getData() == null)
			return ResponseConstants.FAILURE;
		for (UnitOfMeasure c : lcd.getData()) {
			create(c);
		}
		return ResponseConstants.SUCCESS;

	}

	@Override
	@RequestMapping("/find")
	public @ResponseBody
	CRUDResponseWrapper<UnitOfMeasure> find(String query)
			throws RESTControllerException {
		// TODO not implemented yet
		return null;
	}

	@Override
	@RequestMapping(value = "/filterby", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody
	CRUDResponseWrapper<UnitOfMeasure> find(@RequestParam("attributename") String  attName, @RequestParam("valueLike") String valueLike)
			throws RESTControllerException {
		//support class only
		List<UnitOfMeasure> cds = unitOfMeasureService.getByClass(valueLike);
		CRUDResponseWrapper<UnitOfMeasure> ret = new CRUDResponseWrapper<UnitOfMeasure>();
		ret.setCount(cds.size());
		ret.setTotalCount(cds.size());
		ret.setData(cds);
		return ret;
	}
}
