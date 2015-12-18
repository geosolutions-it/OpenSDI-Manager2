package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "species")
@Table(name = "species")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "species")
@XmlRootElement(name = "species")
public class Species {

    @Id
    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "authority")
    private String authority;

    @Column(name = "cofc")
    private Integer cofc;

    @Column(name = "tolerance")
    private String tolerance;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "family")
    private String ind;

    @Column(name = "hydro")
    private String hydro;

    @Column(name = "form")
    private String form;

    @Column(name = "habit")
    private String habit;

    @Column(name = "groupp")
    private String groupp;

    @Column(name = "shade")
    private String nativity;

    @Column(name = "code1")
    private String code1;

    @Column(name = "code2")
    private String code2;

    @Column(name = "code3")
    private String code3;

    @Column(name = "code4")
    private String code4;

    @Column(name = "code5")
    private String code5;

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getCofc() {
        return cofc;
    }

    public void setCofc(Integer cofc) {
        this.cofc = cofc;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

    public String getHydro() {
        return hydro;
    }

    public void setHydro(String hydro) {
        this.hydro = hydro;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getGroupp() {
        return groupp;
    }

    public void setGroupp(String groupp) {
        this.groupp = groupp;
    }

    public String getNativity() {
        return nativity;
    }

    public void setNativity(String nativity) {
        this.nativity = nativity;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getCode4() {
        return code4;
    }

    public void setCode4(String code4) {
        this.code4 = code4;
    }

    public String getCode5() {
        return code5;
    }

    public void setCode5(String code5) {
        this.code5 = code5;
    }
}
