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
