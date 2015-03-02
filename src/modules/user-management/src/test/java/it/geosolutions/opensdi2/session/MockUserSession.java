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

import java.util.Calendar;

import org.springframework.security.core.userdetails.UserDetails;

import it.geosolutions.opensdi2.session.impl.UserSessionImpl;

public class MockUserSession extends UserSessionImpl {
	
	private Calendar mockExpiration; 
	
	public MockUserSession(String id, UserDetails user, Calendar expiration) {
		super(id, user, expiration);
	}

	public MockUserSession(UserDetails user, Calendar expiration) {
		super(user, expiration);
	}

	@Override
	public void setExpiration(Calendar expiration) {
		super.setExpiration(expiration);
		mockExpiration = expiration;
	}

	public Calendar getExpiration() {
		return mockExpiration;
	}
	
}
