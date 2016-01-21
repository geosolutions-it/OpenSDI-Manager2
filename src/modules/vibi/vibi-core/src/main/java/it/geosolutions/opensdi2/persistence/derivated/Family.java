package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "family")
@Table(name = "family")
@XmlRootElement(name = "family")
public class Family {

    @Id
    @Column(name = "family")
    private String family;
}
