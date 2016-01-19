package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "biomass")
@Table(name = "biomass")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "biomass")
@XmlRootElement(name = "biomass")
public class Biomass {

    @Id
    @Column(name = "fid")
    private String fid;

    @Column(name = "plot_no")
    private Integer plotNo;

    @Column(name = "date_time")
    private Date date_time;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "corner")
    private Integer corner;

    @Column(name = "sample_id")
    private Integer sampleId;

    @Column(name = "area_sampled")
    private Double areaSampled;

    @Column(name = "weight_with_bag")
    private Double weightWithBag;

    @Column(name = "bag_weight")
    private Double bagWeight;

    @Column(name = "biomass_collected")
    private String biomassCollected;

    @Column(name = "biomass_weight_grams")
    private Double biomassWeightGrams;

    @Column(name = "grams_per_sq_meter")
    private Double gramsPerSqMeter;

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

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
    }

    public Integer getSampleId() {
        return sampleId;
    }

    public void setSampleId(Integer sampleId) {
        this.sampleId = sampleId;
    }

    public Double getAreaSampled() {
        return areaSampled;
    }

    public void setAreaSampled(Double areaSampled) {
        this.areaSampled = areaSampled;
    }

    public Double getWeightWithBag() {
        return weightWithBag;
    }

    public void setWeightWithBag(Double weightWithBag) {
        this.weightWithBag = weightWithBag;
    }

    public Double getBagWeight() {
        return bagWeight;
    }

    public void setBagWeight(Double bagWeight) {
        this.bagWeight = bagWeight;
    }

    public String getBiomassCollected() {
        return biomassCollected;
    }

    public void setBiomassCollected(String biomassCollected) {
        this.biomassCollected = biomassCollected;
    }

    public Double getBiomassWeightGrams() {
        return biomassWeightGrams;
    }

    public void setBiomassWeightGrams(Double biomassWeightGrams) {
        this.biomassWeightGrams = biomassWeightGrams;
    }

    public Double getGramsPerSqMeter() {
        return gramsPerSqMeter;
    }

    public void setGramsPerSqMeter(Double gramsPerSqMeter) {
        this.gramsPerSqMeter = gramsPerSqMeter;
    }
}
