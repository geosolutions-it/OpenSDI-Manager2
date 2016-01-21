package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "CODE4")
@Table(name = "CODE4")
@XmlRootElement(name = "CODE4")
public class Code4 {

    @Id
    @Column(name = "CODE4")
    private String CODE4;
}
