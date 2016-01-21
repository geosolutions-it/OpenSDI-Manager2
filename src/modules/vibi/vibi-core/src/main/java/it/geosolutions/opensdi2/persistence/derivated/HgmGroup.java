package it.geosolutions.opensdi2.persistence.derivated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "hgm_group")
@Table(name = "hgm_group")
@XmlRootElement(name = "hgm_group")
public class HgmGroup {

    @Id
    @Column(name = "hgm_group")
    private String hgmGroup;

    public String getHgmGroup() {
        return hgmGroup;
    }

    public void setHgmGroup(String hgmGroup) {
        this.hgmGroup = hgmGroup;
    }
}
