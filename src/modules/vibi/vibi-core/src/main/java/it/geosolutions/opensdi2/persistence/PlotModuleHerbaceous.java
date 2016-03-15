package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "plot_module_herbaceous")
@Table(name = "plot_module_herbaceous")
@XmlRootElement(name = "plot_module_herbaceous")
public class PlotModuleHerbaceous {

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
