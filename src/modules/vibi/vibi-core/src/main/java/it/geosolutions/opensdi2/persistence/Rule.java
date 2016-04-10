package it.geosolutions.opensdi2.persistence;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "rule")
@XmlRootElement
public final class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "allow")
    private Boolean allow;

    @Column(name = "_user")
    private String user;

    @Column(name = "_group")
    private String group;

    @Column(name = "service")
    private String service;

    @Column(name = "operation")
    private String operation;

    @Column(name = "entity")
    private String entity;

    @Column(name = "format")
    private String format;

    @Column(name = "size")
    private Long size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getAllow() {
        return allow;
    }

    public void setAllow(Boolean allow) {
        this.allow = allow;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
