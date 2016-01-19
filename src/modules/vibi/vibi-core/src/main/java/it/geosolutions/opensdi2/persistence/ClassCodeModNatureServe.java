package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "class_code_Mod_NatureServe")
@Table(name = "class_code_Mod_NatureServe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "class_code_Mod_NatureServe")
@XmlRootElement(name = "class_code_Mod_NatureServe")
public class ClassCodeModNatureServe {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "community_class")
    private String communityClass;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCommunityClass() {
        return communityClass;
    }

    public void setCommunityClass(String communityClass) {
        this.communityClass = communityClass;
    }
}
