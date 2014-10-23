/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
@XmlRootElement
public class StatsBean {

    public static enum CLASSIFIER_TYPE {
        PROVINCE,
        DISTRICT,
        CUSTOM;
    }

    public static enum MASK_TYPE {
        STANDARD,
        CUSTOM,
        DISABLED;
    }

    private String ndviFileName;

    private CLASSIFIER_TYPE classifier;
    private String classifierFullPath;

    private MASK_TYPE forestMask;
    private String forestMaskFullPath;

    @XmlElement
    public String getNdviFileName() {
        return ndviFileName;
    }

    public void setNdviFileName(String ndviFileName) {
        this.ndviFileName = ndviFileName;
    }

    @XmlElement
    public CLASSIFIER_TYPE getClassifier() {
        return classifier;
    }

    public void setClassifier(CLASSIFIER_TYPE classifier) {
        this.classifier = classifier;
    }

    @XmlElement
    public String getClassifierFullPath() {
        return classifierFullPath;
    }

    public void setClassifierFullPath(String classifierFullPath) {
        this.classifierFullPath = classifierFullPath;
    }

    @XmlElement
    public MASK_TYPE getForestMask() {
        return forestMask;
    }

    public void setForestMask(MASK_TYPE mask) {
        this.forestMask = mask;
    }

    @XmlElement
    public String getForestMaskFullPath() {
        return forestMaskFullPath;
    }

    public void setForestMaskFullPath(String forestMaskFullPath) {
        this.forestMaskFullPath = forestMaskFullPath;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName())
                .append('[');
        sb.append("ndvifile:").append(ndviFileName);
        sb.append(" clsT:").append(classifier);
        if(classifierFullPath != null)
            sb.append(" clsFile:").append(classifierFullPath);
        sb.append(" mskT:").append(forestMask);

        if(forestMaskFullPath != null)
            sb.append(" mskFile:").append(forestMaskFullPath);

        sb.append(']');
        return sb.toString();
    }



}
