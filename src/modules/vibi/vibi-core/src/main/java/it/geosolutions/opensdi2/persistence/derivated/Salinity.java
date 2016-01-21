package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "salinity")
@Table(name = "salinity")
@XmlRootElement(name = "salinity")
public class Salinity {

    @Id
    @Column(name = "salinity")
    private String salinity;
}
