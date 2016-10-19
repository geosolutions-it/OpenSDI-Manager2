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
package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "biomass")
@Table(name = "biomass")
@XmlRootElement(name = "biomass")
public class Biomass {

    @Id
    @Column(name = "view_id")
    private Integer viewId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "plot_id")
    Plot plot;

    public String getPlotNo() {
        return plot.getPlotNo();
    }

    public String getMonitoringEvent() {
        return plot.getMonitoringEvent();
    }

    public Date getDateTimer() {
        return plot.getDateTimer();
    }

    @Column(name = "date_time")
    private Date date_time;

    @Column(name = "module_id")
    private String moduleId;

    @Column(name = "corner")
    private String corner;

    @Column(name = "sample_id")
    private Integer sampleId;

    @Column(name = "area_sampled")
    private Double areaSampled;

    @Column(name = "weight_with_bag")
    private Double weightWithBag;

    @Column(name = "bag_weight")
    private Double bagWeight;

    @Column(name = "biomass_collected")
    private String biomassCollected;

    @Column(name = "biomass_weight_grams")
    private Double biomassWeightGrams;

    @Column(name = "grams_per_sq_meter")
    private Double gramsPerSqMeter;

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }

    public Double getAreaSampled() {
        return areaSampled;
    }

    public void setAreaSampled(Double areaSampled) {
        this.areaSampled = areaSampled;
    }

    public Double getWeightWithBag() {
        return weightWithBag;
    }

    public void setWeightWithBag(Double weightWithBag) {
        this.weightWithBag = weightWithBag;
    }

    public Double getBagWeight() {
        return bagWeight;
    }

    public void setBagWeight(Double bagWeight) {
        this.bagWeight = bagWeight;
    }

    public String getBiomassCollected() {
        return biomassCollected;
    }

    public void setBiomassCollected(String biomassCollected) {
        this.biomassCollected = biomassCollected;
    }

    public Double getBiomassWeightGrams() {
        return biomassWeightGrams;
    }

    public void setBiomassWeightGrams(Double biomassWeightGrams) {
        this.biomassWeightGrams = biomassWeightGrams;
    }

    public Double getGramsPerSqMeter() {
        return gramsPerSqMeter;
    }

    public void setGramsPerSqMeter(Double gramsPerSqMeter) {
        this.gramsPerSqMeter = gramsPerSqMeter;
    }
}
