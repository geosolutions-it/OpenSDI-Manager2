package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "CODE1")
@Table(name = "CODE1")
@XmlRootElement(name = "CODE1")
public class Code1 {

    @Id
    @Column(name = "CODE1")
    private String CODE1;
}
