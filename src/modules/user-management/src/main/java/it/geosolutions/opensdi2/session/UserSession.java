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
 * Basic interface for a UserSession.
 * The sesssion has a unique identifier, contains user data and allows for
 * expiration check.
 * 
 * @author Mauro Bartolomeoli
 */
public interface UserSession {
    public String getId();
    
    public UserDetails getUser();
    
    public void setId(String id);
    
    public void setUser(UserDetails user);
    
    public boolean isExpired();
}
