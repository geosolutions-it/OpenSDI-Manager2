package it.geosolutions.opensdi.dto;

import sun.net.www.http.KeepAliveCache;
import it.geosolutions.opensdi.model.CropDescriptor;
import it.geosolutions.opensdi.model.Season;

/**
 * A rest object that can be serialized
 * in JSON
 * @author Lorenzo Natali, GeoSolutions
 *
 */
public class RESTCropDescriptor {

		String id;
	    

		String label;
	    

		boolean RABI = false; 
		boolean KHARIF = false;


		private String prod_default_unit;


		private String area_default_unit;


		private String yield_default_unit;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public boolean isRABI() {
			return RABI;
		}
		public void setRABI(boolean rABI) {
			RABI = rABI;
		}
		public boolean isKHARIF() {
			return KHARIF;
		}
		public void setKHARIF(boolean kHARIF) {
			KHARIF = kHARIF;
		}
		
		public String getProd_default_unit() {
			return prod_default_unit;
		}

		public void setProd_default_unit(String prod_default_unit) {
			this.prod_default_unit = prod_default_unit;
		}

		public String getArea_default_unit() {
			return area_default_unit;
		}

		public void setArea_default_unit(String area_default_unit) {
			this.area_default_unit = area_default_unit;
		}

		public String getYield_default_unit() {
			return yield_default_unit;
		}

		public void setYield_default_unit(String yield_default_unit) {
			this.yield_default_unit = yield_default_unit;
		}
		
		public RESTCropDescriptor(){
			
		}
		
		public RESTCropDescriptor(CropDescriptor cd ){
			id = cd.getId();
			label =  cd.getLabel();
			String[] s = cd.getSeasons();
			prod_default_unit = cd.getProd_default_unit();
			yield_default_unit = cd.getYield_default_unit();
			area_default_unit = cd.getArea_default_unit();	
			for(int i = 0 ; i < s.length ; i++){
				if(Season.Names.RABI.equals(s[i])){
					RABI = true;
				}
				if(Season.Names.KHARIF.equals(s[i])){
					KHARIF = true;
				}
			}
			
		}
		/**
		 * Create a crop descriptor for the RESTCropDescriptor
		 * @return
		 */
		public CropDescriptor toCropDescriptor(){
			CropDescriptor c = new CropDescriptor();
			c.setId(id);
			c.setLabel(label);
			c.setProd_default_unit(prod_default_unit);
			c.setArea_default_unit(area_default_unit);
			c.setYield_default_unit(yield_default_unit);
			Season s =null;
			if (RABI || KHARIF){
				if(RABI && KHARIF){
					c.setSeasons(Season.RABI_KHARIF);
					
				}else if(RABI){
					c.setSeasons(Season.RABI);
				}else{
					c.setSeasons(Season.KHARIF);
				}
			}else{
				c.setSeasons(Season.NONE);
			}
			
			return c;
			
		}
}
