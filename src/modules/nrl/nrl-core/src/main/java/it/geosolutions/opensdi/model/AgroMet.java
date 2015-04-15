package it.geosolutions.opensdi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = "AgroMet")
@Table(name = "agromet", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"factor" , "district" , "province" , "year", "month", "dec"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "agromet")
@XmlRootElement(name = "AgroMet")
public class AgroMet {

    @GeneratedValue
    @Id
    private Long id;

//	Long rowid;// integer,
    @Column(nullable = false, updatable = false)
    private String district ;//character varying(255) NOT NULL,

    @Column(nullable = false, updatable = false)
    private String province;// character varying(255) NOT NULL,

    @Column(nullable = false, updatable = false)
    private int year;// integer NOT NULL,

    @Column(nullable = false, updatable = false)
    private String month;// character varying(3) NOT NULL,

    @Column(nullable = false, updatable = false)
    private int dec;// TODO to dek if possible in table"dec" bigint NOT NULL,

    @Column(nullable = false, updatable = false)
    private String factor; //factor character varying(255) NOT NULL,

    @Column
    private double value;//; double precision,
    @Column
    private int s_yr;// integer,
    @Column
    private int s_dec;// integer
    @Column(nullable = false)
    private int absolute_dek;
   
	@Column(nullable = false)
    private int dek_in_year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDec() {
        return dec;
    }

    public void setDec(int dec) {
        this.dec = dec;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getS_yr() {
        return s_yr;
    }

    public void setS_yr(int s_yr) {
        this.s_yr = s_yr;
    }

    public int getS_dec() {
        return s_dec;
    }

    public void setS_dec(int s_dec) {
        this.s_dec = s_dec;
    }

    public int getAbsolute_dek() {
		return absolute_dek;
	}

	public void setAbsolute_dek(int absolute_dek) {
		this.absolute_dek = absolute_dek;
	}

	public int getDek_in_year() {
		return dek_in_year;
	}

	public void setDek_in_year(int dek_in_year) {
		this.dek_in_year = dek_in_year;
	}

    @Override
    public String toString() {
        return "AgroMet{" + "id=" + id + ", dist=" + district + ", prov=" + province 
                + ", yr=" + year + ", mn=" + month + ", dec=" + dec
                + ", factor=" + factor + ", value=" + value
                + ", s_yr=" + s_yr + ", s_dec=" + s_dec + '}';
    }


}
