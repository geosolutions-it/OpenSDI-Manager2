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
@XmlRootElement(name = "species")
public class Species {

    @Column(name = "veg_id")
    private Integer vegId;

    @Id
    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "authority")
    private String authority;

    @Column(name = "cofc")
    private Integer cofc;

    @Column(name = "syn")
    private String syn;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "family")
    private String family;

    @Column(name = "fn")
    private Integer fn;

    @Column(name = "wet")
    private String wet;

    @Column(name = "form")
    private String form;

    @Column(name = "habit")
    private String habit;

    @Column(name = "shade")
    private String shade;

    @Column(name = "usda_id")
    private String usdaId;

    @Column(name = "oh_tore")
    private String ohTore;

    @Column(name = "type")
    private String type;

    @Column(name = "oh_status")
    private String ohStatus;

    @Column(name = "emp")
    private String emp;

    @Column(name = "mw")
    private String mw;

    @Column(name = "ncne")
    private String ncne;

    @Column(name = "notes")
    private String notes;

    public Integer getVegId() {
        return vegId;
    }

    public void setVegId(Integer vegId) {
        this.vegId = vegId;
    }

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

    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Integer getFn() {
        return fn;
    }

    public void setFn(Integer fn) {
        this.fn = fn;
    }

    public String getWet() {
        return wet;
    }

    public void setWet(String wet) {
        this.wet = wet;
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

    public String getShade() {
        return shade;
    }

    public void setShade(String shade) {
        this.shade = shade;
    }

    public String getUsdaId() {
        return usdaId;
    }

    public void setUsdaId(String usdaId) {
        this.usdaId = usdaId;
    }

    public String getOhTore() {
        return ohTore;
    }

    public void setOhTore(String ohTore) {
        this.ohTore = ohTore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOhStatus() {
        return ohStatus;
    }

    public void setOhStatus(String ohStatus) {
        this.ohStatus = ohStatus;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getMw() {
        return mw;
    }

    public void setMw(String mw) {
        this.mw = mw;
    }

    public String getNcne() {
        return ncne;
    }

    public void setNcne(String ncne) {
        this.ncne = ncne;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
