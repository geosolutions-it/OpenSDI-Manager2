/**
 *
 */
package it.geosolutions.opensdi.service;

import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.UnitOfMeasure;
import it.geosolutions.opensdi.persistence.dao.CropDescriptorDAO;
import it.geosolutions.opensdi.persistence.dao.UnitOfMeasureDAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for units of measure.
 * Provide a service tier for unit of measure support for crops.
 * @author Lorenzo Natali
 *
 */
@Transactional(value = "opensdiTransactionManager")
public class UnitOfMeasureService {

    @Autowired
    private UnitOfMeasureDAO unitOfMeasureDao;

    @Autowired
    private CropDescriptorDAO cropDescriptorDao;
    

	public CropDescriptorDAO getCropDescriptorDao() {
		return cropDescriptorDao;
	}

	public void setCropDescriptorDao(CropDescriptorDAO cropDescriptorDao) {
		this.cropDescriptorDao = cropDescriptorDao;
	}

	public UnitOfMeasureDAO getUnitOfMeasureDao() {
		return unitOfMeasureDao;
	}

	public void setUnitOfMeasureDao(UnitOfMeasureDAO unitOfMeasureDao) {
		this.unitOfMeasureDao = unitOfMeasureDao;
	}

	public List<UnitOfMeasure> getAll() {
        return unitOfMeasureDao.findAll();
    }

    public void persist(UnitOfMeasure cd) {
    	unitOfMeasureDao.persist(cd);
    }

    public UnitOfMeasure get(String id) {
        return unitOfMeasureDao.find(id);
    }

    public void update(UnitOfMeasure c) {
    	unitOfMeasureDao.merge(c);
    }

    public boolean delete(String id) {
    	return unitOfMeasureDao.removeById(id);
    }

	public List<UnitOfMeasure> getByClass(String valueLike) {
		// TODO Auto-generated method stub
		return unitOfMeasureDao.getByClass(valueLike);
		
	}
	public long getCount(){
		return unitOfMeasureDao.count(null);
	}
	
	public UnitOfMeasure getDefaultProductionUnitOfMeasure(String crop){
		return getUnitForCrop(crop,VariableName.PRODUCTION);
	}
	public UnitOfMeasure getDefaultAreaUnitOfMeasure(String crop){
		return getUnitForCrop(crop,VariableName.AREA);
	}
	public UnitOfMeasure getDefaultYieldUnitOfMeasure(String crop){
		return getUnitForCrop(crop,VariableName.YIELD);
	}
	
	public UnitOfMeasure getDefaultProductionUnitOfMeasure(CropDescriptor crop){
		return getUnitForCrop(crop.getId(), VariableName.PRODUCTION);
	}
	public UnitOfMeasure getDefaultAreaUnitOfMeasure(CropDescriptor crop){
		return getUnitForCrop(crop.getId(), VariableName.AREA);
	}
	public UnitOfMeasure getDefaultYieldUnitOfMeasure(CropDescriptor crop){
		return getUnitForCrop(crop.getId(), VariableName.YIELD);
	}
	
	/**
	 * Returns the default unit of measure for the proper variable and crop
	 * @param crop crop Id
	 * @param variable variable
	 * @return the default unit of measure
	 */
	private UnitOfMeasure getUnitForCrop(String crop,VariableName variable){
		CropDescriptor cd = cropDescriptorDao.find(crop);
		switch (variable) {
		case AREA:
			String area = cd.getArea_default_unit();
			return unitOfMeasureDao.find(area);

		case PRODUCTION:
			String prod = cd.getProd_default_unit();
			return unitOfMeasureDao.find(prod);

		case YIELD:
			String yield = cd.getYield_default_unit();
			return unitOfMeasureDao.find(yield);

		default:
			return null;

		}
	}
	private enum VariableName {
		AREA,PRODUCTION,YIELD
	}
}
