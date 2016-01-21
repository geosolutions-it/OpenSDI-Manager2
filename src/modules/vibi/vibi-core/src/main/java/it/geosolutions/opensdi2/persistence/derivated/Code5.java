package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "CODE5")
@Table(name = "CODE5")
@XmlRootElement(name = "CODE5")
public class Code5 {

    @Id
    @Column(name = "CODE5")
    private String CODE5;
}
