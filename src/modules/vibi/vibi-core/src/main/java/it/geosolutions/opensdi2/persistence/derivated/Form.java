package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "form")
@Table(name = "form")
@XmlRootElement(name = "form")
public class Form {

    @Id
    @Column(name = "form")
    private String form;
}
