package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "authority")
@Table(name = "authority")
@XmlRootElement(name = "authority")
public class Authority {

    @Id
    @Column(name = "authority")
    private String authority;
}
