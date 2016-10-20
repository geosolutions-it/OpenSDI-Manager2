/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity(name = "metric_calculations")
@Table(name = "metric_calculations")
@XmlRootElement(name = "metric_calculations")
public class Metrics {

    @Id
    @Column(name = "view_id")
    private Integer viewId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "plot_id")
    Plot plot;

    public String getPlotNo() {
        return plot.getPlotNo();
    }

    public String getMonitoringEvent() {
        return plot.getMonitoringEvent();
    }

    public Date getDateTimer() {
        return plot.getDateTimer();
    }

    @Column(name = "vibi_type")
    private String vibiType;

    @Column(name = "score")
    private Double score;

    @Column(name = "carex_metric_value")
    private Double carexMetricValue;

    @Column(name = "cyperaceae_metric_value")
    private Double cyperaceaeMetricValue;

    @Column(name = "dicot_metric_value")
    private Double dicotMetricValue;

    @Column(name = "shade_metric_value")
    private Double shadeMetricValue;

    @Column(name = "shrub_metric_value")
    private Double shrubMetricValue;

    @Column(name = "hydrophyte_metric_value")
    private Double hydrophyteMetricValue;

    @Column(name = "svp_metric_value")
    private Double svpMetricValue;

    @Column(name = "ap_ratio_metric_value")
    private Double apRatioMetricValue;

    @Column(name = "fqai_metric_value")
    private Double fqaiMetricValue;

    @Column(name = "bryophyte_metric_value")
    private Double bryophyteMetricValue;

    @Column(name = "per_hydrophyte_metric_value")
    private Double perHydrophyteMetricValue;

    @Column(name = "sensitive_metric_value")
    private Double sensitiveMetricValue;

    @Column(name = "tolerant_metric_value")
    private Double tolerantMetricValue;

    @Column(name = "invasive_graminoids_metric_value")
    private Double invasiveGraminoidsMetricValue;

    @Column(name = "small_tree_metric_value")
    private Double small_tree;

    @Column(name = "subcanopy_iv")
    private Double subcanopyIv;

    @Column(name = "canopy_iv")
    private Double canopyIv;

    @Column(name = "biomass_metric_value")
    private Double biomassMetricValue;

    @Column(name = "stems_ha_wetland_trees")
    private Double stemsHaWetlandTrees;

    @Column(name = "stems_ha_wetland_shrubs")
    private Double stemsHaWetlandShrubs;

    @Column(name = "per_unvegetated")
    private Double perUnvegetated;

    @Column(name = "per_button_bush")
    private Double perButtonBush;

    @Column(name = "per_perennial_native_hydrophytes")
    private Double perPerennialNativeHydrophytes;

    @Column(name = "per_adventives")
    private Double perAdventives;

    @Column(name = "per_open_water")
    private Double perPpenWater;

    @Column(name = "per_unvegetated_open_water")
    private Double perUnvegetatedOpenWater;

    @Column(name = "per_bare_ground")
    private Double perBareGround;

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public String getVibiType() {
        return vibiType;
    }

    public void setVibiType(String vibiType) {
        this.vibiType = vibiType;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getCarexMetricValue() {
        return carexMetricValue;
    }

    public void setCarexMetricValue(Double carexMetricValue) {
        this.carexMetricValue = carexMetricValue;
    }

    public Double getCyperaceaeMetricValue() {
        return cyperaceaeMetricValue;
    }

    public void setCyperaceaeMetricValue(Double cyperaceaeMetricValue) {
        this.cyperaceaeMetricValue = cyperaceaeMetricValue;
    }

    public Double getDicotMetricValue() {
        return dicotMetricValue;
    }

    public void setDicotMetricValue(Double dicotMetricValue) {
        this.dicotMetricValue = dicotMetricValue;
    }

    public Double getShadeMetricValue() {
        return shadeMetricValue;
    }

    public void setShadeMetricValue(Double shadeMetricValue) {
        this.shadeMetricValue = shadeMetricValue;
    }

    public Double getShrubMetricValue() {
        return shrubMetricValue;
    }

    public void setShrubMetricValue(Double shrubMetricValue) {
        this.shrubMetricValue = shrubMetricValue;
    }

    public Double getHydrophyteMetricValue() {
        return hydrophyteMetricValue;
    }

    public void setHydrophyteMetricValue(Double hydrophyteMetricValue) {
        this.hydrophyteMetricValue = hydrophyteMetricValue;
    }

    public Double getSvpMetricValue() {
        return svpMetricValue;
    }

    public void setSvpMetricValue(Double svpMetricValue) {
        this.svpMetricValue = svpMetricValue;
    }

    public Double getApRatioMetricValue() {
        return apRatioMetricValue;
    }

    public void setApRatioMetricValue(Double apRatioMetricValue) {
        this.apRatioMetricValue = apRatioMetricValue;
    }

    public Double getFqaiMetricValue() {
        return fqaiMetricValue;
    }

    public void setFqaiMetricValue(Double fqaiMetricValue) {
        this.fqaiMetricValue = fqaiMetricValue;
    }

    public Double getBryophyteMetricValue() {
        return bryophyteMetricValue;
    }

    public void setBryophyteMetricValue(Double bryophyteMetricValue) {
        this.bryophyteMetricValue = bryophyteMetricValue;
    }

    public Double getPerHydrophyteMetricValue() {
        return perHydrophyteMetricValue;
    }

    public void setPerHydrophyteMetricValue(Double perHydrophyteMetricValue) {
        this.perHydrophyteMetricValue = perHydrophyteMetricValue;
    }

    public Double getSensitiveMetricValue() {
        return sensitiveMetricValue;
    }

    public void setSensitiveMetricValue(Double sensitiveMetricValue) {
        this.sensitiveMetricValue = sensitiveMetricValue;
    }

    public Double getTolerantMetricValue() {
        return tolerantMetricValue;
    }

    public void setTolerantMetricValue(Double tolerantMetricValue) {
        this.tolerantMetricValue = tolerantMetricValue;
    }

    public Double getInvasiveGraminoidsMetricValue() {
        return invasiveGraminoidsMetricValue;
    }

    public void setInvasiveGraminoidsMetricValue(Double invasiveGraminoidsMetricValue) {
        this.invasiveGraminoidsMetricValue = invasiveGraminoidsMetricValue;
    }

    public Double getSmall_tree() {
        return small_tree;
    }

    public void setSmall_tree(Double small_tree) {
        this.small_tree = small_tree;
    }

    public Double getSubcanopyIv() {
        return subcanopyIv;
    }

    public void setSubcanopyIv(Double subcanopyIv) {
        this.subcanopyIv = subcanopyIv;
    }

    public Double getCanopyIv() {
        return canopyIv;
    }

    public void setCanopyIv(Double canopyIv) {
        this.canopyIv = canopyIv;
    }

    public Double getBiomassMetricValue() {
        return biomassMetricValue;
    }

    public void setBiomassMetricValue(Double biomassMetricValue) {
        this.biomassMetricValue = biomassMetricValue;
    }

    public Double getStemsHaWetlandTrees() {
        return stemsHaWetlandTrees;
    }

    public void setStemsHaWetlandTrees(Double stemsHaWetlandTrees) {
        this.stemsHaWetlandTrees = stemsHaWetlandTrees;
    }

    public Double getStemsHaWetlandShrubs() {
        return stemsHaWetlandShrubs;
    }

    public void setStemsHaWetlandShrubs(Double stemsHaWetlandShrubs) {
        this.stemsHaWetlandShrubs = stemsHaWetlandShrubs;
    }

    public Double getPerUnvegetated() {
        return perUnvegetated;
    }

    public void setPerUnvegetated(Double perUnvegetated) {
        this.perUnvegetated = perUnvegetated;
    }

    public Double getPerButtonBush() {
        return perButtonBush;
    }

    public void setPerButtonBush(Double perButtonBush) {
        this.perButtonBush = perButtonBush;
    }

    public Double getPerPerennialNativeHydrophytes() {
        return perPerennialNativeHydrophytes;
    }

    public void setPerPerennialNativeHydrophytes(Double perPerennialNativeHydrophytes) {
        this.perPerennialNativeHydrophytes = perPerennialNativeHydrophytes;
    }

    public Double getPerAdventives() {
        return perAdventives;
    }

    public void setPerAdventives(Double perAdventives) {
        this.perAdventives = perAdventives;
    }

    public Double getPerPpenWater() {
        return perPpenWater;
    }

    public void setPerPpenWater(Double perPpenWater) {
        this.perPpenWater = perPpenWater;
    }

    public Double getPerUnvegetatedOpenWater() {
        return perUnvegetatedOpenWater;
    }

    public void setPerUnvegetatedOpenWater(Double perUnvegetatedOpenWater) {
        this.perUnvegetatedOpenWater = perUnvegetatedOpenWater;
    }

    public Double getPerBareGround() {
        return perBareGround;
    }

    public void setPerBareGround(Double perBareGround) {
        this.perBareGround = perBareGround;
    }
}
