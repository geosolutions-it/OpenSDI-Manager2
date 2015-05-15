/*
 *  OpenSDI Manager 2
 *  Copyright (C) 2014 - 2015 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.workflow.action;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Configuration of the {@link FeatureUpdater} action
 * Extends {@link DataStoreConfiguration} adding an output feature and an attribute mapping
 * @author Lorenzo Pini (lorenzo.pini@geo-solutions.it)
 */
@JsonIgnoreProperties({ "dataStore"})
public class FeatureUpdaterConfiguration extends DataStoreConfiguration {

    public String surveyLinkingField = "MY_ORIG_ID";
    
    public String itemLinkingField = "GCID";

    private Map<String,String> rules = new HashMap<String, String>();
    
    /**
     * @return the surveyLinkingField
     */
    public String getSurveyLinkingField() {
        return surveyLinkingField;
    }

    /**
     * @param surveyLinkingField the surveyLinkingField to set
     */
    public void setSurveyLinkingField(String surveyLinkingField) {
        this.surveyLinkingField = surveyLinkingField;
    }

    /**
     * @return the itemLinkingField
     */
    public String getItemLinkingField() {
        return itemLinkingField;
    }

    /**
     * @param itemLinkingField the itemLinkingField to set
     */
    public void setItemLinkingField(String itemLinkingField) {
        this.itemLinkingField = itemLinkingField;
    }
    
    public Map<String,String> getRules() {
        return rules;
    }

    public void setRules(Map<String,String> rules) {
        this.rules = rules;
    }
  
}
