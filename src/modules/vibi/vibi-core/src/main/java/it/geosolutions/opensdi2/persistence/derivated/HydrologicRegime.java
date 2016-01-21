package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "hydrologic_regime")
@Table(name = "hydrologic_regime")
@XmlRootElement(name = "hydrologic_regime")
public class HydrologicRegime {

    @Id
    @Column(name = "hydrologic_regime")
    private String hydrologicRegime;
}
