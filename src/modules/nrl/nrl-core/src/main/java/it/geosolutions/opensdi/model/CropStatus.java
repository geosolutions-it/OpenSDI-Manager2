/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Crop status entity
 * 
 * @author adiaz
 *
 */
@Entity(name = "CropStatus")
@Table(name = "cropstatus", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"month" , "dec" , "factor" , "crop"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "cropstatus")
@XmlRootElement(name = "CropStatus")
public class CropStatus {

    @GeneratedValue
    @Id
    private Long rowId;
    
    @Column(nullable = false, updatable = false)
    private String factor;
    @Column(nullable = false, updatable = false)
    private String crop;
    @Column(nullable = false, updatable = false)
    private String month;
    @Column(nullable = false, updatable = false)
    private Integer dec;
    @Column
    private Double max;
    @Column
    private Double min;
    @Column
    private Double opt;
    @Column
    private Integer s_dec;

	@PrePersist
    @PreUpdate
    public void checkConstraints() {
        if(month == null || dec == null || factor == null || crop == null)
            throw new IllegalStateException("Some PK keys is null");
    }

    public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	public String getCrop() {
		return crop;
	}

	public void setCrop(String crop) {
		this.crop = crop;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getDec() {
		return dec;
	}

	public void setDec(Integer dec) {
		this.dec = dec;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getOpt() {
		return opt;
	}

	public void setOpt(Double opt) {
		this.opt = opt;
	}

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }
    
    public Integer getS_dec() {
		return s_dec;
	}

	public void setS_dec(Integer s_dec) {
		this.s_dec = s_dec;
	}

    @Override
    public String toString() {
		return "CropStatus[rowId=" + rowId + ", factor=" + factor + ", crop="
				+ crop + ", month=" + month + ", dec=" + dec + ", max=" + max
				+ ", min=" + min + ", opt=" + opt +  ", s_dec=" + s_dec + ']';
    }
    
}
