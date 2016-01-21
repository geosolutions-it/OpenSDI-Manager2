package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "depth")
@Table(name = "depth")
@XmlRootElement(name = "depth")
public class Depth {

    @Id
    @Column(name = "depth")
    private Integer depth;
}
