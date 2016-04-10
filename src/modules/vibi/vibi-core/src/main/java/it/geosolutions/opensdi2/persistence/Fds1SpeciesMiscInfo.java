package it.geosolutions.opensdi2.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "fds1_species_misc_info")
@Table(name = "fds1_species_misc_info")
@XmlRootElement(name = "fds1_species_misc_info")
public class Fds1SpeciesMiscInfo {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "species")
    private String species;

    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "voucher_no")
    private String voucherNo;

    @Column(name = "comment")
    private String comment;

    @Column(name = "browse_intensity")
    private String browseIntensity;

    @Column(name = "percent_flowering")
    private String percentFlowering;

    @Column(name = "percent_fruiting")
    private String percentFruiting;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
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

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBrowseIntensity() {
        return browseIntensity;
    }

    public void setBrowseIntensity(String browseIntensity) {
        this.browseIntensity = browseIntensity;
    }

    public String getPercentFlowering() {
        return percentFlowering;
    }

    public void setPercentFlowering(String percentFlowering) {
        this.percentFlowering = percentFlowering;
    }

    public String getPercentFruiting() {
        return percentFruiting;
    }

    public void setPercentFruiting(String percentFruiting) {
        this.percentFruiting = percentFruiting;
    }
}
