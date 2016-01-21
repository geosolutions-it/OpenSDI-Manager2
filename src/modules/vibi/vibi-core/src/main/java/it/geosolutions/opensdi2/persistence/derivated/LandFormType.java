package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "landform_type")
@Table(name = "landform_type")
@XmlRootElement(name = "landform_type")
public class LandFormType {

    @Id
    @Column(name = "landform_type")
    private String landformType;

    public String getLandformType() {
        return landformType;
    }

    public void setLandformType(String landformType) {
        this.landformType = landformType;
    }
}
