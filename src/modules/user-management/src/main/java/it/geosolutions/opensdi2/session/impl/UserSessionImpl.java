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

import java.util.Calendar;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Basic implementation of UserSession.
 * 
 * @author Mauro Bartolomeoli
 *
 */
public class UserSessionImpl implements UserSession {

    private String id;
    
    private UserDetails user;
    
    private Calendar expiration;
    
    private long expirationInterval = 0l;
    
    public UserSessionImpl(String id, UserDetails user, Calendar expiration) {
        super();
        this.id = id;
        this.user = user;
        this.expiration = expiration;
    }
    
    public UserSessionImpl(UserDetails user, Calendar expiration) {
        super();
        this.user = user;
        this.setExpiration(expiration);
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public void setExpiration(Calendar expiration) {
        this.expiration = expiration;
        if(expiration != null) {
        	expirationInterval = expiration.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        }
    }

    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public UserDetails getUser() {
        return user;
    }
    
    @Override
    public boolean isExpired() {
        if(expiration != null) {
            return expiration.getTime().before(Calendar.getInstance().getTime());
        }
        return false;
    }

	@Override
	public void refresh() {
		if(expiration != null) {
			Calendar newExpiration = Calendar.getInstance();
			newExpiration.setTimeInMillis(newExpiration.getTimeInMillis() + expirationInterval);
			setExpiration(newExpiration);
		}
		
	}

    
}
