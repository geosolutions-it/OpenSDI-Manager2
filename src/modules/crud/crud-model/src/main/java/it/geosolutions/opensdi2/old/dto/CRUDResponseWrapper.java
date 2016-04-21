/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
