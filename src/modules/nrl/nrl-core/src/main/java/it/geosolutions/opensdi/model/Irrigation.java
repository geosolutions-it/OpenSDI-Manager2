/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author DamianoG
 *
 */
@Entity(name = "Irrigation")
@Table(name = "irrigation", uniqueConstraints = { @UniqueConstraint(columnNames = { "year","month","decade","province","district","river" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "irrigation")
@XmlRootElement(name = "Irrigation")
public class Irrigation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(updatable = true, nullable = false)    
    private Integer year;
    
    @Column(updatable = true, nullable = false)    
    private String month;
    
    @Column(updatable = true, nullable = false)    
    private Integer decade;
    
    @Column(name="decade_year", updatable = true, nullable = false)    
    private Integer decadeYear;
    
    @Column(name="decade_absolute", updatable = true, nullable = false)    
    private Integer decadeAbsolute;
    
    @Column(updatable = true, nullable = false)    
    private String province;
    
    @Column(updatable = true, nullable = false)    
    private String district;
    
    @Column(updatable = true, nullable = false)    
    private String river;

    @Column(updatable = true, nullable = true)    
    private Integer withdrawal;
    
    @Column(updatable = true, nullable = true)    
    private Integer waterflow;
    
    @PrePersist
    @PreUpdate
    public void checkFunctionalConstraints() {
        if(withdrawal != null && waterflow != null){
            throw new IllegalStateException("withdrawal and waterflow fields are both valorized... this is not valid...");
        }
        if(withdrawal != null){
            if(province == null || province.isEmpty()){
                throw new IllegalStateException("withdrawal field valorized but province field not... this is not valid...");
            }
        }
        else{
            if(river == null || river.isEmpty()){
                throw new IllegalStateException("waterflow field valorized but river field not... this is not valid...");
            }
        }
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @return the decade
     */
    public Integer getDecade() {
        return decade;
    }

    /**
     * @param decade the decade to set
     */
    public void setDecade(Integer decade) {
        this.decade = decade;
    }

    /**
     * @return the decadeYear
     */
    public Integer getDecadeYear() {
        return decadeYear;
    }

    /**
     * @param decadeYear the decadeYear to set
     */
    public void setDecadeYear(Integer decadeYear) {
        this.decadeYear = decadeYear;
    }

    /**
     * @return the decadeAbsolute
     */
    public Integer getDecadeAbsolute() {
        return decadeAbsolute;
    }

    /**
     * @param decadeAbsolute the decadeAbsolute to set
     */
    public void setDecadeAbsolute(Integer decadeAbsolute) {
        this.decadeAbsolute = decadeAbsolute;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the river
     */
    public String getRiver() {
        return river;
    }

    /**
     * @param river the river to set
     */
    public void setRiver(String river) {
        this.river = river;
    }

    /**
     * @return the withdrawal
     */
    public Integer getWithdrawal() {
        return withdrawal;
    }

    /**
     * @param withdrawal the withdrawal to set
     */
    public void setWithdrawal(Integer withdrawal) {
        this.withdrawal = withdrawal;
    }

    /**
     * @return the waterflow
     */
    public Integer getWaterflow() {
        return waterflow;
    }

    /**
     * @param waterflow the waterflow to set
     */
    public void setWaterflow(Integer waterflow) {
        this.waterflow = waterflow;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }
    
    
}
