package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "plot")
@Table(name = "plot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "plot")
@XmlRootElement(name = "plot")
public class Plot {

    @Id
    @Column(name = "plot_no")
    private Long plotNo;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "plot_name")
    private String plotName;

    @Column(name = "plot_label")
    private String plotLabel;

    @Column(name = "monitoring_event")
    private String monitoringEvent;

    @Column(name = "datetimer")
    private Date dateTimer;

    @Column(name = "party")
    private String party;

    @Column(name = "plot_not_sampled")
    private String plotNotSampled;

    @Column(name = "commentplot_not_sampled")
    private String commentPlotNotSampled;

    @Column(name = "sampling_quality")
    private String samplingQuality;

    @Column(name = "tax_accuracy_vascular")
    private String taxAccuracyVascular;

    @Column(name = "tax_accuracy_bryophytes")
    private String taxAccuracyBryophytes;

    @Column(name = "tax_accuracy_lichens")
    private String taxAccuracyLichens;

    @Column(name = "authority")
    private String authority;

    @Column(name = "state")
    private String state;

    @Column(name = "county")
    private String county;

    @Column(name = "quadrangle")
    private String quadrangle;

    @Column(name = "local_place_name")
    private String localPlaceName;

    @Column(name = "landowner")
    private String landOwner;

    @Column(name = "xaxis_bearing_of_plot")
    private Integer xaxisBearingOfPlot;

    @Column(name = "enter_gps_location_in_plot")
    private String enterGpsLocationInPlot;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "total_modules")
    private Integer totalModule;

    @Column(name = "intensive_modules")
    private Integer intensiveModules;

    @Column(name = "plot_configuration")
    private String plotConfiguration;

    @Column(name = "plot_size_for_cover_data_area_ha")
    private Double plotSizeForCoverDataAreaHa;

    @Column(name = "estimate_of_per_open_water_entire_site")
    private Double estimateOfPerOpenWaterEntireSite;

    @Column(name = "estimate_of_perunvegetated_ow_entire_site")
    private Double estimateOfPerunvegetatedOwEntireSite;

    @Column(name = "Estimate_per_invasives_entire_site")
    private Double estimatePerInvasivesEntireSite;

    @Column(name = "centerline")
    private Double centerline;

    @Column(name = "oneo_plant")
    private String oneoPlant;

    @Column(name = "oneo_text")
    private String oneoText;

    @Column(name = "vegclass")
    private String vegclass;

    @Column(name = "vegsubclass")
    private String vegsubclass;

    @Column(name = "twoo_plant")
    private String twooPlant;

    @Column(name = "hgmclass")
    private String hgmclass;

    @Column(name = "hgmsubclass")
    private String hgmsubclass;

    @Column(name = "twoo_hgm")
    private String twooHgm;

    @Column(name = "hgmgroup")
    private String hgmgroup;

    @Column(name = "oneo_class_code_mod_natureServe")
    private String oneoClassCodeModNatureServe;

    @Column(name = "veg_class_wetlands_only")
    private String vegClassWetlandsOnly;

    @Column(name = "landform_type")
    private String landformType;

    @Column(name = "homogeneity")
    private String homogeneity;

    @Column(name = "stand_size")
    private String standSize;

    @Column(name = "drainage")
    private String drainage;

    @Column(name = "salinity")
    private String salinity;

    @Column(name = "hydrologic_regime")
    private String hydrologicRegime;

    @Column(name = "oneo_disturbance_type")
    private String oneoDisturbanceType;

    @Column(name = "oneo_disturbance_severity")
    private String oneoDisturbanceSeverity;

    @Column(name = "oneo_disturbance_years_ago")
    private Integer oneoDisturbanceYearsAgo;

    @Column(name = "oneo_distubance_per_of_plot")
    private Integer oneoDistubancePerOfPlot;

    @Column(name = "oneo_disturbance_description")
    private String oneoDisturbanceDescription;

    @Column(name = "twoo_disturbance_type")
    private String twooDisturbanceType;

    @Column(name = "twoo_disturbance_severity")
    private String twooDisturbanceSeverity;

    @Column(name = "twoo_disturbance_years_ago")
    private Integer twooDisturbanceYearsAgo;

    @Column(name = "twoo_distubance_per_of_plot")
    private Integer twooDistubancePerOfPlot;

    @Column(name = "twoo_disturbance_description")
    private String twooDisturbanceDescription;

    @Column(name = "threeo_disturbance_type")
    private String threeoDisturbanceType;

    @Column(name = "threeo_disturbance_severity")
    private String threeoDisturbanceSeverity;

    @Column(name = "threeo_disturbance_years_ago")
    private Integer threeoDisturbanceYearsAgo;

    @Column(name = "threeo_distubance_per_of_plot")
    private Integer threeoDistubancePerOfPlot;

    @Column(name = "threeo_disturbance_description")
    private String threeoDisturbanceDescription;

    public Long getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(Long plotNo) {
        this.plotNo = plotNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    public String getPlotLabel() {
        return plotLabel;
    }

    public void setPlotLabel(String plotLabel) {
        this.plotLabel = plotLabel;
    }

    public String getMonitoringEvent() {
        return monitoringEvent;
    }

    public void setMonitoringEvent(String monitoringEvent) {
        this.monitoringEvent = monitoringEvent;
    }

    public Date getDateTimer() {
        return dateTimer;
    }

    public void setDateTimer(Date dateTimer) {
        this.dateTimer = dateTimer;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPlotNotSampled() {
        return plotNotSampled;
    }

    public void setPlotNotSampled(String plotNotSampled) {
        this.plotNotSampled = plotNotSampled;
    }

    public String getCommentPlotNotSampled() {
        return commentPlotNotSampled;
    }

    public void setCommentPlotNotSampled(String commentPlotNotSampled) {
        this.commentPlotNotSampled = commentPlotNotSampled;
    }

    public String getSamplingQuality() {
        return samplingQuality;
    }

    public void setSamplingQuality(String samplingQuality) {
        this.samplingQuality = samplingQuality;
    }

    public String getTaxAccuracyVascular() {
        return taxAccuracyVascular;
    }

    public void setTaxAccuracyVascular(String taxAccuracyVascular) {
        this.taxAccuracyVascular = taxAccuracyVascular;
    }

    public String getTaxAccuracyBryophytes() {
        return taxAccuracyBryophytes;
    }

    public void setTaxAccuracyBryophytes(String taxAccuracyBryophytes) {
        this.taxAccuracyBryophytes = taxAccuracyBryophytes;
    }

    public String getTaxAccuracyLichens() {
        return taxAccuracyLichens;
    }

    public void setTaxAccuracyLichens(String taxAccuracyLichens) {
        this.taxAccuracyLichens = taxAccuracyLichens;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getQuadrangle() {
        return quadrangle;
    }

    public void setQuadrangle(String quadrangle) {
        this.quadrangle = quadrangle;
    }

    public String getLocalPlaceName() {
        return localPlaceName;
    }

    public void setLocalPlaceName(String localPlaceName) {
        this.localPlaceName = localPlaceName;
    }

    public String getLandOwner() {
        return landOwner;
    }

    public void setLandOwner(String landOwner) {
        this.landOwner = landOwner;
    }

    public Integer getXaxisBearingOfPlot() {
        return xaxisBearingOfPlot;
    }

    public void setXaxisBearingOfPlot(Integer xaxisBearingOfPlot) {
        this.xaxisBearingOfPlot = xaxisBearingOfPlot;
    }

    public String getEnterGpsLocationInPlot() {
        return enterGpsLocationInPlot;
    }

    public void setEnterGpsLocationInPlot(String enterGpsLocationInPlot) {
        this.enterGpsLocationInPlot = enterGpsLocationInPlot;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalModule() {
        return totalModule;
    }

    public void setTotalModule(Integer totalModule) {
        this.totalModule = totalModule;
    }

    public Integer getIntensiveModules() {
        return intensiveModules;
    }

    public void setIntensiveModules(Integer intensiveModules) {
        this.intensiveModules = intensiveModules;
    }

    public String getPlotConfiguration() {
        return plotConfiguration;
    }

    public void setPlotConfiguration(String plotConfiguration) {
        this.plotConfiguration = plotConfiguration;
    }

    public Double getPlotSizeForCoverDataAreaHa() {
        return plotSizeForCoverDataAreaHa;
    }

    public void setPlotSizeForCoverDataAreaHa(Double plotSizeForCoverDataAreaHa) {
        this.plotSizeForCoverDataAreaHa = plotSizeForCoverDataAreaHa;
    }

    public Double getEstimateOfPerOpenWaterEntireSite() {
        return estimateOfPerOpenWaterEntireSite;
    }

    public void setEstimateOfPerOpenWaterEntireSite(Double estimateOfPerOpenWaterEntireSite) {
        this.estimateOfPerOpenWaterEntireSite = estimateOfPerOpenWaterEntireSite;
    }

    public Double getEstimateOfPerunvegetatedOwEntireSite() {
        return estimateOfPerunvegetatedOwEntireSite;
    }

    public void setEstimateOfPerunvegetatedOwEntireSite(Double estimateOfPerunvegetatedOwEntireSite) {
        this.estimateOfPerunvegetatedOwEntireSite = estimateOfPerunvegetatedOwEntireSite;
    }

    public Double getEstimatePerInvasivesEntireSite() {
        return estimatePerInvasivesEntireSite;
    }

    public void setEstimatePerInvasivesEntireSite(Double estimatePerInvasivesEntireSite) {
        this.estimatePerInvasivesEntireSite = estimatePerInvasivesEntireSite;
    }

    public Double getCenterline() {
        return centerline;
    }

    public void setCenterline(Double centerline) {
        this.centerline = centerline;
    }

    public String getOneoPlant() {
        return oneoPlant;
    }

    public void setOneoPlant(String oneoPlant) {
        this.oneoPlant = oneoPlant;
    }

    public String getOneoText() {
        return oneoText;
    }

    public void setOneoText(String oneoText) {
        this.oneoText = oneoText;
    }

    public String getVegclass() {
        return vegclass;
    }

    public void setVegclass(String vegclass) {
        this.vegclass = vegclass;
    }

    public String getVegsubclass() {
        return vegsubclass;
    }

    public void setVegsubclass(String vegsubclass) {
        this.vegsubclass = vegsubclass;
    }

    public String getTwooPlant() {
        return twooPlant;
    }

    public void setTwooPlant(String twooPlant) {
        this.twooPlant = twooPlant;
    }

    public String getHgmclass() {
        return hgmclass;
    }

    public void setHgmclass(String hgmclass) {
        this.hgmclass = hgmclass;
    }

    public String getHgmsubclass() {
        return hgmsubclass;
    }

    public void setHgmsubclass(String hgmsubclass) {
        this.hgmsubclass = hgmsubclass;
    }

    public String getTwooHgm() {
        return twooHgm;
    }

    public void setTwooHgm(String twooHgm) {
        this.twooHgm = twooHgm;
    }

    public String getHgmgroup() {
        return hgmgroup;
    }

    public void setHgmgroup(String hgmgroup) {
        this.hgmgroup = hgmgroup;
    }

    public String getOneoClassCodeModNatureServe() {
        return oneoClassCodeModNatureServe;
    }

    public void setOneoClassCodeModNatureServe(String oneoClassCodeModNatureServe) {
        this.oneoClassCodeModNatureServe = oneoClassCodeModNatureServe;
    }

    public String getVegClassWetlandsOnly() {
        return vegClassWetlandsOnly;
    }

    public void setVegClassWetlandsOnly(String vegClassWetlandsOnly) {
        this.vegClassWetlandsOnly = vegClassWetlandsOnly;
    }

    public String getLandformType() {
        return landformType;
    }

    public void setLandformType(String landformType) {
        this.landformType = landformType;
    }

    public String getHomogeneity() {
        return homogeneity;
    }

    public void setHomogeneity(String homogeneity) {
        this.homogeneity = homogeneity;
    }

    public String getStandSize() {
        return standSize;
    }

    public void setStandSize(String standSize) {
        this.standSize = standSize;
    }

    public String getDrainage() {
        return drainage;
    }

    public void setDrainage(String drainage) {
        this.drainage = drainage;
    }

    public String getSalinity() {
        return salinity;
    }

    public void setSalinity(String salinity) {
        this.salinity = salinity;
    }

    public String getHydrologicRegime() {
        return hydrologicRegime;
    }

    public void setHydrologicRegime(String hydrologicRegime) {
        this.hydrologicRegime = hydrologicRegime;
    }

    public String getOneoDisturbanceType() {
        return oneoDisturbanceType;
    }

    public void setOneoDisturbanceType(String oneoDisturbanceType) {
        this.oneoDisturbanceType = oneoDisturbanceType;
    }

    public String getOneoDisturbanceSeverity() {
        return oneoDisturbanceSeverity;
    }

    public void setOneoDisturbanceSeverity(String oneoDisturbanceSeverity) {
        this.oneoDisturbanceSeverity = oneoDisturbanceSeverity;
    }

    public Integer getOneoDisturbanceYearsAgo() {
        return oneoDisturbanceYearsAgo;
    }

    public void setOneoDisturbanceYearsAgo(Integer oneoDisturbanceYearsAgo) {
        this.oneoDisturbanceYearsAgo = oneoDisturbanceYearsAgo;
    }

    public Integer getOneoDistubancePerOfPlot() {
        return oneoDistubancePerOfPlot;
    }

    public void setOneoDistubancePerOfPlot(Integer oneoDistubancePerOfPlot) {
        this.oneoDistubancePerOfPlot = oneoDistubancePerOfPlot;
    }

    public String getOneoDisturbanceDescription() {
        return oneoDisturbanceDescription;
    }

    public void setOneoDisturbanceDescription(String oneoDisturbanceDescription) {
        this.oneoDisturbanceDescription = oneoDisturbanceDescription;
    }

    public String getTwooDisturbanceType() {
        return twooDisturbanceType;
    }

    public void setTwooDisturbanceType(String twooDisturbanceType) {
        this.twooDisturbanceType = twooDisturbanceType;
    }

    public String getTwooDisturbanceSeverity() {
        return twooDisturbanceSeverity;
    }

    public void setTwooDisturbanceSeverity(String twooDisturbanceSeverity) {
        this.twooDisturbanceSeverity = twooDisturbanceSeverity;
    }

    public Integer getTwooDisturbanceYearsAgo() {
        return twooDisturbanceYearsAgo;
    }

    public void setTwooDisturbanceYearsAgo(Integer twooDisturbanceYearsAgo) {
        this.twooDisturbanceYearsAgo = twooDisturbanceYearsAgo;
    }

    public Integer getTwooDistubancePerOfPlot() {
        return twooDistubancePerOfPlot;
    }

    public void setTwooDistubancePerOfPlot(Integer twooDistubancePerOfPlot) {
        this.twooDistubancePerOfPlot = twooDistubancePerOfPlot;
    }

    public String getTwooDisturbanceDescription() {
        return twooDisturbanceDescription;
    }

    public void setTwooDisturbanceDescription(String twooDisturbanceDescription) {
        this.twooDisturbanceDescription = twooDisturbanceDescription;
    }

    public String getThreeoDisturbanceType() {
        return threeoDisturbanceType;
    }

    public void setThreeoDisturbanceType(String threeoDisturbanceType) {
        this.threeoDisturbanceType = threeoDisturbanceType;
    }

    public String getThreeoDisturbanceSeverity() {
        return threeoDisturbanceSeverity;
    }

    public void setThreeoDisturbanceSeverity(String threeoDisturbanceSeverity) {
        this.threeoDisturbanceSeverity = threeoDisturbanceSeverity;
    }

    public Integer getThreeoDisturbanceYearsAgo() {
        return threeoDisturbanceYearsAgo;
    }

    public void setThreeoDisturbanceYearsAgo(Integer threeoDisturbanceYearsAgo) {
        this.threeoDisturbanceYearsAgo = threeoDisturbanceYearsAgo;
    }

    public Integer getThreeoDistubancePerOfPlot() {
        return threeoDistubancePerOfPlot;
    }

    public void setThreeoDistubancePerOfPlot(Integer threeoDistubancePerOfPlot) {
        this.threeoDistubancePerOfPlot = threeoDistubancePerOfPlot;
    }

    public String getThreeoDisturbanceDescription() {
        return threeoDisturbanceDescription;
    }

    public void setThreeoDisturbanceDescription(String threeoDisturbanceDescription) {
        this.threeoDisturbanceDescription = threeoDisturbanceDescription;
    }
}
