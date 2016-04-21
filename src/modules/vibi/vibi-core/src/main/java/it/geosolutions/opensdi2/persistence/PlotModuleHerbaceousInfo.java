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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "plot_module_herbaceous_info")
@Table(name = "plot_module_herbaceous_info")
@XmlRootElement(name = "plot_module_herbaceous_info")
public class PlotModuleHerbaceousInfo {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "corner")
    private Integer cornerId;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "info")
    private String info;

    @Column(name = "cover_class_code")
    private Integer coverClassCode;

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

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCornerId() {
        return cornerId;
    }

    public void setCornerId(Integer cornerId) {
        this.cornerId = cornerId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getCoverClassCode() {
        return coverClassCode;
    }

    public void setCoverClassCode(Integer coverClassCode) {
        this.coverClassCode = coverClassCode;
    }
}
