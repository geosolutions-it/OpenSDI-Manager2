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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author DamianoG
 * 
 */
@Entity(name = "Fertilizer")
@Table(name = "fertilizer", uniqueConstraints = { @UniqueConstraint(columnNames = { "province",
        "district", "year", "month", "nutrient" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "fertilizer")
@XmlRootElement(name = "Fertilizer")
public class Fertilizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = true, nullable = false)
    private String province;

    @Column(updatable = true, nullable = false)
    private String district;

    @Column(updatable = true, nullable = false)
    private Integer year;

    @Column(updatable = true, nullable = false)
    private String month;

    @Column(updatable = true, nullable = false)
    private String nutrient;

    @Column(name = "offtake_tons", updatable = true, nullable = false)
    private Double offtakeTons;

    @Column(name = "month_num", updatable = true, nullable = false)
    private Integer monthNum;
    
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
     * @return the nutrient
     */
    public String getNutrient() {
        return nutrient;
    }

    /**
     * @param nutrient the nutrient to set
     */
    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    /**
     * @return the offtakeTons
     */
    public Double getOfftakeTons() {
        return offtakeTons;
    }

    /**
     * @param offtakeTons the offtakeTons to set
     */
    public void setOfftakeTons(Double offtakeTons) {
        this.offtakeTons = offtakeTons;
    }

    /**
     * @return the monthNum
     */
    public Integer getMonthNum() {
        return monthNum;
    }

    /**
     * @param monthNum the monthNum to set
     */
    public void setMonthNum(Integer monthNum) {
        this.monthNum = monthNum;
    }

}
