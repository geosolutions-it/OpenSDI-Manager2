package it.geosolutions.opensdi2.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "Plot")
@Table(name = "plot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "plot")
@XmlRootElement(name = "Plot")
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
}
