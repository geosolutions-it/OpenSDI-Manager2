/*
 *  OpenSDI Manager
 *  Copyright (C) 2014 GeoSolutions S.A.S.
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
package it.geosolutions.opensdi2.session.impl;

import it.geosolutions.opensdi2.session.UserSession;
import it.geosolutions.opensdi2.session.UserSessionService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * In memory implementation of a UserSessionService.
 * 
 * @author Mauro Bartolomeoli
 *
 */
public class InMemoryUserSessionService implements UserSessionService {

    private Map<String, UserSession> sessions = new ConcurrentHashMap<String, UserSession>();
    private int cleanUpSeconds = 60;

    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);
    
    private Runnable evictionTask = new Runnable() {
        @Override
        public void run() {
            for(String sessionId : sessions.keySet()) {
                UserSession session = sessions.get(sessionId);
                if(session.isExpired()) {
                    removeSession(sessionId);
                }
            }
        }
    };
    
    public InMemoryUserSessionService() {
        super();
        // schedule eviction thread
        scheduler.scheduleAtFixedRate(evictionTask, cleanUpSeconds, cleanUpSeconds,
                TimeUnit.SECONDS);
    }
    
    public void setCleanUpSeconds(int cleanUpSeconds) {
        this.cleanUpSeconds = cleanUpSeconds;
    }



    @Override
    public UserDetails getUserData(String sessionId) {
        if(sessions.containsKey(sessionId)) {
            UserSession session = sessions.get(sessionId);
            if(session.isExpired()) {
                removeSession(sessionId);
                return null;
            }
            return session.getUser();
        }
        return null;
    }
    
    @Override
    public void registerNewSession(String sessionId, UserSession session) {
        sessions.put(sessionId, session);
    }
    
    @Override
    public String registerNewSession(UserSession session) {
        String sessionId = createSessionId();
        session.setId(sessionId);
        registerNewSession(sessionId, session);
        return sessionId;
    }
    
    private String createSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
    
   
    @Override
    public void removeAllSessions() {
        sessions.clear();
    }
    
    /**
     * Checks that owner is the user bound to the given sessionId.
     * Ownership is checked by:
     *  - userData equality to the given object
     *  - username equality to the string representation of ownwer
     * 
     * @param sessionId
     * @param owner
     * @return
     */
    public boolean isOwner(String sessionId, Object owner) {
        UserSession session = sessions.get(sessionId);
        if(session != null) {
            return owner.toString().equals(session.getUser().getUsername())
                    || owner.equals(session.getUser());
        }
        return false;
    }

}
