package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "drainage")
@Table(name = "drainage")
@XmlRootElement(name = "drainage")
public class Drainage {

    @Id
    @Column(name = "drainage")
    private String drainage;
}
