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

@Entity(name = "woody_importance_value")
@Table(name = "woody_importance_value")
@XmlRootElement(name = "woody_importance_value")
public class WoodyImportanceValue {

    @Id
    @Column(name = "view_id")
    private Integer viewId;

    @Column(name = "plot_no")
    private Integer plotNo;

    @Column(name = "species")
    private String species;

    @Column(name = "subcanopy_iv_partial")
    private Double subcanopyIvPartial;

    @Column(name = "subcanopy_iv_shade")
    private Double subcanopyIvShade;

    @Column(name = "canopy_iv")
    private Double canopyIv;

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public Integer getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(Integer plotNo) {
        this.plotNo = plotNo;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Double getSubcanopyIvPartial() {
        return subcanopyIvPartial;
    }

    public void setSubcanopyIvPartial(Double subcanopyIvPartial) {
        this.subcanopyIvPartial = subcanopyIvPartial;
    }

    public Double getSubcanopyIvShade() {
        return subcanopyIvShade;
    }

    public void setSubcanopyIvShade(Double subcanopyIvShade) {
        this.subcanopyIvShade = subcanopyIvShade;
    }

    public Double getCanopyIv() {
        return canopyIv;
    }

    public void setCanopyIv(Double canopyIv) {
        this.canopyIv = canopyIv;
    }
}
