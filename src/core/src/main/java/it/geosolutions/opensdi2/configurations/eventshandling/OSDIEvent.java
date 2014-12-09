/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.configurations.eventshandling;

/**
 * A class for handle an OSDIEvent
 * 
 * @author DamianoG
 * 
 */
public class OSDIEvent implements Event {

    private String eventID;
    private String scopeID;
    private String instanceID;
    private String eventMessage;
    private Object eventObject;

    public OSDIEvent(String eventID, String scopeID, String instanceID) {
        this.eventID = eventID;
        this.scopeID = scopeID;
        this.instanceID = instanceID;
        this.eventMessage = "No message has been set for this event";
        this.eventMessage = null;
    }

    /**
     * @return the eventID
     */
    @Override
    public String getEventID() {
        return eventID;
    }

    /**
     * @param eventID the eventID to set
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * @return the scopeID
     */
    public String getScopeID() {
        return scopeID;
    }

    /**
     * @param scopeID the scopeID to set
     */
    public void setScopeID(String scopeID) {
        this.scopeID = scopeID;
    }

    /**
     * @return the instanceID
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * @param instanceID the instanceID to set
     */
    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }
    
    /**
     * Utility method to generate an unique Event ID
     * The output will be in the form:
     * &lt;scopeID&gt;-&lt;instanceID&gt;-&lt;Math.random()&gt;
     * 
     * @param scopeID
     * @param instanceID
     * @return
     */
    public static String generateEventID(String scopeID, String instanceID){
        StringBuilder sb = new StringBuilder();
        sb.append(scopeID);     
        sb.append("-");
        sb.append(instanceID);
        sb.append("-");
        sb.append(Math.random());
        return Integer.toString(sb.hashCode());
    }

    @Override
    public String getEventMessage() {
        return this.eventMessage;
    }

    @Override
    public Object getEventObject() {
        return this.eventObject;
    }

    /**
     * @param eventMessage the eventMessage to set
     */
    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    /**
     * @param eventObject the eventObject to set
     */
    public void setEventObject(Object eventObject) {
        this.eventObject = eventObject;
    }
    
    
}
