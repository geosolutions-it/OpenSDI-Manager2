package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "CODE3")
@Table(name = "CODE3")
@XmlRootElement(name = "CODE3")
public class Code3 {

    @Id
    @Column(name = "CODE3")
    private String CODE3;
}
