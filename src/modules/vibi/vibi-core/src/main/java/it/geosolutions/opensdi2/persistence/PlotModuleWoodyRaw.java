package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "plot_module_woody_raw")
@Table(name = "plot_module_woody_raw")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "plot_module_woody_raw")
@XmlRootElement(name = "plot_module_woody_raw")
public class PlotModuleWoodyRaw {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "plot_no")
    private Integer plotNo;

    @Column(name = "sub")
    private Double sub;

    @Column(name = "module_id")
    private Integer module_id;

    @Column(name = "species")
    private String species;

    @Column(name = "dbh_class")
    private String dbhClass;

    @Column(name = "dbh_class_index")
    private Integer dbhClassIndex;

    @Column(name = "count")
    private String count;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(Integer plotNo) {
        this.plotNo = plotNo;
    }

    public Double getSub() {
        return sub;
    }

    public void setSub(Double sub) {
        this.sub = sub;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
