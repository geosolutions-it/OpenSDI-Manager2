package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "homogeneity")
@Table(name = "homogeneity")
@XmlRootElement(name = "homogeneity")
public class Homogeneity {

    @Id
    @Column(name = "homogeneity")
    private String homogeneity;
}
