package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "hgm_subclass")
@Table(name = "hgm_subclass")
@XmlRootElement(name = "hgm_subclass")
public class HgmSubClass {

    @Id
    @Column(name = "hgm_subclass")
    private String hgmSubClass;

    public String getHgmSubClass() {
        return hgmSubClass;
    }

    public void setHgmSubClass(String hgmSubClass) {
        this.hgmSubClass = hgmSubClass;
    }
}
