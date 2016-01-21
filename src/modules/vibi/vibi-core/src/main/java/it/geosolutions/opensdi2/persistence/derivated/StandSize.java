package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "stand_size")
@Table(name = "stand_size")
@XmlRootElement(name = "stand_size")
public class StandSize {

    @Id
    @Column(name = "stand_size")
    private String standSize;
}
