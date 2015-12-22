package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "class_code_mod_natureserve")
@Table(name = "class_code_mod_natureserve")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "class_code_mod_natureserve")
@XmlRootElement(name = "class_code_mod_natureserve")
public class ClassCodeModNatureServer {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "veg_class")
    private String vegClass;

    @Column(name = "veg_group")
    private String vegGroup;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVegClass() {
        return vegClass;
    }

    public void setVegClass(String vegClass) {
        this.vegClass = vegClass;
    }

    public String getVegGroup() {
        return vegGroup;
    }

    public void setVegGroup(String vegGroup) {
        this.vegGroup = vegGroup;
    }
}
