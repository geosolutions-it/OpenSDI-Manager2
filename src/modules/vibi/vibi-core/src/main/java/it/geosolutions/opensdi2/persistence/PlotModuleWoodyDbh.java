package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "plot_module_woody_dbh")
@Table(name = "plot_module_woody_dbh")
@XmlRootElement(name = "plot_module_woody_dbh")
public class PlotModuleWoodyDbh {

    @Id
    @Column(name = "view_id")
    private Integer viewId;

    @Column(name = "plot_no")
    private Integer plotNo;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "species")
    private String species;

    @Column(name = "dbh_class")
    private String dbhClass;

    @Column(name = "dbh_class_index")
    private Integer dbhClassIndex;

    @Column(name = "count")
    private Double count;

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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
