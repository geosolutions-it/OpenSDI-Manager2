package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "shade")
@Table(name = "shade")
@XmlRootElement(name = "shade")
public class Shade {

    @Id
    @Column(name = "shade")
    private String shade;
}
