package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "module")
@Table(name = "module")
@XmlRootElement(name = "module")
public class Module {

    @Id
    @Column(name = "module_id")
    private Integer module_id;
}
