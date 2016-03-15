package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "oh_status")
@Table(name = "oh_status")
@XmlRootElement(name = "oh_status")
public class OhStatus {

    @Id
    @Column(name = "status")
    private String status;
}
