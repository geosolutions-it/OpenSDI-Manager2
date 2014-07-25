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
package it.geosolutions.opensdi2.session;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Basic interface for a UserSession service.
 * The service should allow registering new sessions, verifying them, removing and automatic expiring.
 * 
 * @author Mauro Bartolomeoli
 */
public interface UserSessionService {
    public UserDetails getUserData(String sessionId);
    
    /**
     * Register a new session. The session id is given.
     * 
     * @param sessionId
     * @param session
     */
    public void registerNewSession(String sessionId, UserSession session);
    
    /**
     * Register a new session. The session id is automatically created and returned.
     * 
     * @param session
     * @return the generated session id
     */
    public String registerNewSession(UserSession session);
    
    /**
     * Remove a session, given its id.
     * @param sessionId
     */
    public void removeSession(String sessionId);
    
    
    /**
     * Remove all the sessions.
     */
    public void removeAllSessions();
    
    /**
     * Checks that owner is the user bound to the given sessionId.
     * 
     * @param sessionId
     * @param owner
     * @return
     */
    public boolean isOwner(String sessionId, Object owner);
}
