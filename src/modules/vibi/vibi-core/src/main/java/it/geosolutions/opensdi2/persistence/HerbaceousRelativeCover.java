package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "herbaceous_relative_cover")
@Table(name = "herbaceous_relative_cover")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "herbaceous_relative_cover")
@XmlRootElement(name = "herbaceous_relative_cover")
public class HerbaceousRelativeCover {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "plot_no")
    private Integer plotNo;

    @Column(name = "species")
    private String species;

    @Column(name = "relative_cover")
    private Double relativeCover;

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

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Double getRelativeCover() {
        return relativeCover;
    }

    public void setRelativeCover(Double relativeCover) {
        this.relativeCover = relativeCover;
    }
}
