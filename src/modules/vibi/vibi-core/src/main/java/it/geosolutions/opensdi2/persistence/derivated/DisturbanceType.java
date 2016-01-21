package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "disturbance_type")
@Table(name = "disturbance_type")
@XmlRootElement(name = "disturbance_type")
public class DisturbanceType {

    @Id
    @Column(name = "disturbance_type")
    private String disturbanceType;
}
