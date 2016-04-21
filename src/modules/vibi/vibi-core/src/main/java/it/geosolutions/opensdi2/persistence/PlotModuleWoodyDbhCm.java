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

@Entity(name = "plot_module_woody_dbh_cm")
@Table(name = "plot_module_woody_dbh_cm")
@XmlRootElement(name = "plot_module_woody_dbh_cm")
public class PlotModuleWoodyDbhCm {

    @Id
    @Column(name = "view_id")
    private Integer viewId;

    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "species")
    private String species;

    @Column(name = "dbh_class")
    private String dbhClass;

    @Column(name = "dbh_class_index")
    private Integer dbhClassIndex;

    @Column(name = "dbh_cm")
    private Double dbhCm;

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDbhClass() {
        return dbhClass;
    }

    public void setDbhClass(String dbhClass) {
        this.dbhClass = dbhClass;
    }

    public Integer getDbhClassIndex() {
        return dbhClassIndex;
    }

    public void setDbhClassIndex(Integer dbhClassIndex) {
        this.dbhClassIndex = dbhClassIndex;
    }

    public Double getDbhCm() {
        return dbhCm;
    }

    public void setDbhCm(Double dbhCm) {
        this.dbhCm = dbhCm;
    }
}
