package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "habit")
@Table(name = "habit")
@XmlRootElement(name = "habit")
public class Habit {

    @Id
    @Column(name = "habit")
    private String habit;
}
