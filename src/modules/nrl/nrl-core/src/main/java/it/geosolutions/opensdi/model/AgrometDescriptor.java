package it.geosolutions.opensdi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = "agrometdescriptor")
@Table(name = "agrometdescriptor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "agrometdescriptor")
@XmlRootElement(name = "agrometdescriptor")
public class AgrometDescriptor {
    @Id
	String factor;
    
    @Column(updatable=true,nullable=false)
	String label;
    
    @Column(updatable=true,nullable=false)
	String aggregation; // ???
    
    @Column(updatable=true,nullable=false)
    String unit; // ???
	
	public String getFactor() {
		return factor;
	}
	public void setFactor(String id) {
		this.factor = id;
	}
	public String getAggregation() {
        return aggregation;
    }
    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
