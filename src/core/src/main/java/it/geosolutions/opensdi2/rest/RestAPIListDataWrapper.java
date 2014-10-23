/**
 * 
 */
package it.geosolutions.opensdi2.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author alessio.fabiani
 *
 */
@JsonInclude(Include.NON_NULL)
public class RestAPIListDataWrapper<T> {

	private Map<String , Object> otherProperties = new HashMap<String , Object>();
	
	private List<T> data;
	private int count;
	private long  totalCount;
	
	/**
	 * @return the data 
	 */
	public List<T> getData() {
		return data;
	}
	
	/**
	 * Set the data
	 * @param data
	 */
	public void setData(List<T> data) {
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
	
	@JsonAnyGetter
    public Map<String , Object> any() {
        return otherProperties;
    }
 
    @JsonAnySetter
    public void set(String name, Object value) {
        otherProperties.put(name, value);
    }
}
