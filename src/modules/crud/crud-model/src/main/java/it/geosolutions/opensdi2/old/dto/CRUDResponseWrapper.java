package it.geosolutions.opensdi2.old.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
/**
 * Wrapper for data of the REST Controller
 * @author Lorenzo Natali,GeoSolutions
 *
 * @param <TYPE> the type of the Resource to wrap.
 */

public class CRUDResponseWrapper<TYPE> {

	private List<TYPE> data;
	private int count;
	private long  totalCount;
	
	/**
	 * @return the data 
	 */
	public List<TYPE> getData() {
		return data;
	}
	
	/**
	 * Set the data
	 * @param data
	 */
	public void setData(List<TYPE> data) {
		this.data = data;
	}
	
	/**
	 * Number of elements
	 * @return
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Set the number of elements
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * @return all the elements available (for pagination)
	 */
	public long getTotalCount() {
		return totalCount;
	}
	
	/**
	 * Set the number of the elements available
	 * @param totalCount elements available (for pagination)
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
}
