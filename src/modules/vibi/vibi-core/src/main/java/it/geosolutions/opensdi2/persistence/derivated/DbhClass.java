package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "dbh_class")
@Table(name = "dbh_class")
@XmlRootElement(name = "dbh_class")
public class DbhClass {

    @Id
    @Column(name = "dbh_class")
    private String dbh_class;
}
