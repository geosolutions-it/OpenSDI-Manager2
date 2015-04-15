package it.geosolutions.opensdi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = "unitofmeasure")
@Table(name = "measure_units")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "unitofmeasure")
@XmlRootElement(name = "unitofmeasure")
public class UnitOfMeasure {
    @Id
    @Column(length = 20)
	String id;
    
    @Column(updatable=true,nullable=false,length = 255)
	String name;
    
    @Column(updatable=true,nullable=false,length = 20)
	String shortname; // ???
    
    @Column(updatable=true,nullable=true,length = 255)
    String description; // ???
    
    @Column(updatable=true,nullable=false,length = 20)
    String cls; // ???
    @Column(updatable=true,nullable=false, columnDefinition = "double precision default 1")
    double coefficient;
    @Column(updatable=true,nullable=true,length = 255)
    String filter;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public double getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}