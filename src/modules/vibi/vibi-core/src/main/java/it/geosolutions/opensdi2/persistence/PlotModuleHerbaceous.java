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

@Entity(name = "plot_module_herbaceous")
@Table(name = "plot_module_herbaceous")
@XmlRootElement(name = "plot_module_herbaceous")
public class PlotModuleHerbaceous {

    @Id
    @Column(name = "fid")
    private String fid;

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

    @Column(name = "module_id")
    private String moduleId;

    @Column(name = "corner")
    private String cornerId;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "species")
    private String species;

    @Column(name = "cover_class_code")
    private Integer coverClassCode;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCornerId() {
        return cornerId;
    }

    public void setCornerId(String cornerId) {
        this.cornerId = cornerId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Integer getCoverClassCode() {
        return coverClassCode;
    }

    public void setCoverClassCode(Integer coverClassCode) {
        this.coverClassCode = coverClassCode;
    }
}
