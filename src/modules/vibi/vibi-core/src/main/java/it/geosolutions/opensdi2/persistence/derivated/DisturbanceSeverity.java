package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "disturbance_severity")
@Table(name = "disturbance_severity")
@XmlRootElement(name = "disturbance_severity")
public class DisturbanceSeverity {

    @Id
    @Column(name = "disturbance_severity")
    private String disturbanceSeverity;
}
