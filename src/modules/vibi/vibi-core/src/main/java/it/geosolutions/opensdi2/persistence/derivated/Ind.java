package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "ind")
@Table(name = "ind")
@XmlRootElement(name = "ind")
public class Ind {

    @Id
    @Column(name = "ind")
    private String ind;
}
