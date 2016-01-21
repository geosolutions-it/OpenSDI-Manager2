package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "nativity")
@Table(name = "nativity")
@XmlRootElement(name = "nativity")
public class Nativity {

    @Id
    @Column(name = "nativity")
    private String nativity;
}
