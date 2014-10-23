package it.geosolutions.opensdi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "MeasureUnit")
@Table(name = "measure_units")
@XmlRootElement(name = "MeasureUnit")
public class MeasureUnit {
	/** serialVersionUID */
    private static final long serialVersionUID = 1L;
    @Id
	String id;
    
    @Column(updatable=true,nullable=false)
	String name;
    
    @Column(updatable=true,nullable=false)
	String shortname;
    
    @Column(updatable=true,nullable=false)
	String description;

}
