package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "biomass_accuracy")
@Table(name = "biomass_accuracy")
@XmlRootElement(name = "biomass_accuracy")
public class BiomassAccuracy {

    @Id
    @Column(name = "biomass_accuracy")
    private String biomass_accuracy;
}
