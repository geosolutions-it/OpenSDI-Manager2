package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "CODE2")
@Table(name = "CODE2")
@XmlRootElement(name = "CODE2")
public class Code2 {

    @Id
    @Column(name = "CODE2")
    private String CODE2;
}
