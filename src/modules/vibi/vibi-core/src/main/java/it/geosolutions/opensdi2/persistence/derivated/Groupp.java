package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "groupp")
@Table(name = "groupp")
@XmlRootElement(name = "groupp")
public class Groupp {

    @Id
    @Column(name = "groupp")
    private String groupp;
}
