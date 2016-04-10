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
@XmlRootElement(name = "plot")
public class Plot {

    @Id
    @Column(name = "plot_no")
    private String plotNo;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "plot_name")
    private String plotName;

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

    @Column(name = "Estimate_per_invasives_entire_site")
    private Double estimatePerInvasivesEntireSite;

    @Column(name = "centerline")
    private Double centerline;

    @Column(name = "vegclass")
    private String vegclass;

    @Column(name = "vegsubclass")
    private String vegsubclass;

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

    @Column(name = "Project_Label")
    private String projectLabel;

    @Column(name = "plot_placement")
    private String plotPlacement;

    @Column(name = "plot_configuration_other")
    private String plotConfigurationOther;

    @Column(name = "Estimate_of_per_unvegetated_ow_entire_site")
    private Double EstimateOfPerUnvegetatedOwEntireSite;

    @Column(name = "leap_landcover_classification")
    private String leapLandcoverClassification;

    @Column(name = "cowardin_classification")
    private String cowardinClassification;

    @Column(name = "cowardin_water_regime")
    private String cowardinWaterRegime;

    @Column(name = "cowardin_special_modifier")
    private String cowardinSpecialModifier;

    @Column(name = "cowardin_special_modifier_other")
    private String cowardinSpecialModifierOther;

    @Column(name = "landscape_position")
    private String landscapePosition;

    @Column(name = "inland_landform")
    private String inlandLandform;

    @Column(name = "water_flow_path")
    private String waterFlowPath;

    @Column(name = "llww_modifiers")
    private String llwwModifiers;

    @Column(name = "llww_modifiers_other")
    private String llwwModifiersOther;

    @Column(name = "landform_type_other")
    private String landformTypeOther;

    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
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

    public String getProjectLabel() {
        return projectLabel;
    }

    public void setProjectLabel(String projectLabel) {
        this.projectLabel = projectLabel;
    }

    public String getPlotPlacement() {
        return plotPlacement;
    }

    public void setPlotPlacement(String plotPlacement) {
        this.plotPlacement = plotPlacement;
    }

    public String getPlotConfigurationOther() {
        return plotConfigurationOther;
    }

    public void setPlotConfigurationOther(String plotConfigurationOther) {
        this.plotConfigurationOther = plotConfigurationOther;
    }

    public Double getEstimateOfPerUnvegetatedOwEntireSite() {
        return EstimateOfPerUnvegetatedOwEntireSite;
    }

    public void setEstimateOfPerUnvegetatedOwEntireSite(Double estimateOfPerUnvegetatedOwEntireSite) {
        EstimateOfPerUnvegetatedOwEntireSite = estimateOfPerUnvegetatedOwEntireSite;
    }

    public String getLeapLandcoverClassification() {
        return leapLandcoverClassification;
    }

    public void setLeapLandcoverClassification(String leapLandcoverClassification) {
        this.leapLandcoverClassification = leapLandcoverClassification;
    }

    public String getCowardinClassification() {
        return cowardinClassification;
    }

    public void setCowardinClassification(String cowardinClassification) {
        this.cowardinClassification = cowardinClassification;
    }

    public String getCowardinWaterRegime() {
        return cowardinWaterRegime;
    }

    public void setCowardinWaterRegime(String cowardinWaterRegime) {
        this.cowardinWaterRegime = cowardinWaterRegime;
    }

    public String getCowardinSpecialModifier() {
        return cowardinSpecialModifier;
    }

    public void setCowardinSpecialModifier(String cowardinSpecialModifier) {
        this.cowardinSpecialModifier = cowardinSpecialModifier;
    }

    public String getCowardinSpecialModifierOther() {
        return cowardinSpecialModifierOther;
    }

    public void setCowardinSpecialModifierOther(String cowardinSpecialModifierOther) {
        this.cowardinSpecialModifierOther = cowardinSpecialModifierOther;
    }

    public String getLandscapePosition() {
        return landscapePosition;
    }

    public void setLandscapePosition(String landscapePosition) {
        this.landscapePosition = landscapePosition;
    }

    public String getInlandLandform() {
        return inlandLandform;
    }

    public void setInlandLandform(String inlandLandform) {
        this.inlandLandform = inlandLandform;
    }

    public String getWaterFlowPath() {
        return waterFlowPath;
    }

    public void setWaterFlowPath(String waterFlowPath) {
        this.waterFlowPath = waterFlowPath;
    }

    public String getLlwwModifiers() {
        return llwwModifiers;
    }

    public void setLlwwModifiers(String llwwModifiers) {
        this.llwwModifiers = llwwModifiers;
    }

    public String getLlwwModifiersOther() {
        return llwwModifiersOther;
    }

    public void setLlwwModifiersOther(String llwwModifiersOther) {
        this.llwwModifiersOther = llwwModifiersOther;
    }

    public String getLandformTypeOther() {
        return landformTypeOther;
    }

    public void setLandformTypeOther(String landformTypeOther) {
        this.landformTypeOther = landformTypeOther;
    }
}
