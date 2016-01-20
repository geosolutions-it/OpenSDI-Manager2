package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "cover_midpoint_lookup")
@Table(name = "cover_midpoint_lookup")
@XmlRootElement(name = "cover_midpoint_lookup")
public class CoverMidpointLookup {

    @Id
    @Column(name = "cover_code")
    private Integer coverCode;

    @Column(name = "midpoint")
    private Double midPoint;

    public Integer getCoverCode() {
        return coverCode;
    }

    public void setCoverCode(Integer coverCode) {
        this.coverCode = coverCode;
    }

    public Double getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(Double midPoint) {
        this.midPoint = midPoint;
    }
}
