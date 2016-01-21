package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "hgm_class")
@Table(name = "hgm_class")
@XmlRootElement(name = "hgm_class")
public class HgmClass {

    @Id
    @Column(name = "hgm_class")
    private String hgmClass;

    public String getHgmClass() {
        return hgmClass;
    }

    public void setHgmClass(String hgmClass) {
        this.hgmClass = hgmClass;
    }
}
