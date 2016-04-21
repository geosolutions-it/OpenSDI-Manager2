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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "biomass_raw")
@Table(name = "biomass_raw")
@XmlRootElement(name = "biomass_raw")
public class BiomassRaw {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "date_time")
    private Date date_time;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "corner")
    private Integer corner;

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

    @Column(name = "actual_or_derived")
    private String actualOrDerived;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
        this.plotNo = plotNo;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
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

    public String getActualOrDerived() {
        return actualOrDerived;
    }

    public void setActualOrDerived(String actualOrDerived) {
        this.actualOrDerived = actualOrDerived;
    }
}
