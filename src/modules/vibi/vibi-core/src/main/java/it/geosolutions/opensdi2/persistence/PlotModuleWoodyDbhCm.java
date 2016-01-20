package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    private Integer plotNo;

    @Column(name = "module_id")
    private Integer module_id;

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

    public Integer getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(Integer plotNo) {
        this.plotNo = plotNo;
    }

    public Integer getModule_id() {
        return module_id;
    }

    public void setModule_id(Integer module_id) {
        this.module_id = module_id;
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
