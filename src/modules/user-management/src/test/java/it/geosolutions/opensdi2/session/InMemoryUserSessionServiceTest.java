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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import it.geosolutions.opensdi2.session.impl.InMemoryUserSessionService;
import it.geosolutions.opensdi2.session.impl.UserSessionImpl;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class InMemoryUserSessionServiceTest {
    private static final String TEST_SESSION_ID = "test_session_id";

    UserSessionService userSessionService;
    
    private UserDetails TEST_USER = new User("test_user", "", new ArrayList<GrantedAuthority>());

    @Before
    public void setUp() {
        userSessionService = new InMemoryUserSessionService();
    }
    
    @Test
    public void testRegisterSessionWithId() {
        userSessionService.registerNewSession(TEST_SESSION_ID, new UserSessionImpl(TEST_USER, null));
        assertNotNull(userSessionService.getUserData(TEST_SESSION_ID));
        assertEquals(TEST_USER, userSessionService.getUserData(TEST_SESSION_ID));
    }
    
    @Test
    public void testRegisterSessionWithoutId() {
        String sessionId = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, null));
        assertNotNull(userSessionService.getUserData(sessionId));
        assertEquals(TEST_USER, userSessionService.getUserData(sessionId));
    }
    
    @Test
    public void testRemoveSession() {
        String sessionId = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, null));
        userSessionService.removeSession(sessionId);
        assertNull(userSessionService.getUserData(sessionId));
    }
    
    @Test
    public void testRemoveAllSessions() {
        String sessionId = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, null));
        String sessionId2 = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, null));
        userSessionService.removeAllSessions();
        assertNull(userSessionService.getUserData(sessionId));
        assertNull(userSessionService.getUserData(sessionId2));
    }
    
    @Test
    public void testIsOwner() {
        String sessionId = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, null));
        assertTrue(userSessionService.isOwner(sessionId, TEST_USER));
        assertTrue(userSessionService.isOwner(sessionId, TEST_USER.getUsername()));
    }
    
    @Test
    public void testExpire() {
        Calendar expire = Calendar.getInstance();
        expire.add(Calendar.MINUTE, -10);
        String sessionId = userSessionService.registerNewSession(new UserSessionImpl(TEST_USER, expire));
        assertNull(userSessionService.getUserData(sessionId));
    }
    
    @Test
    public void testRefresh(){
    	Calendar expire = Calendar.getInstance();
        expire.add(Calendar.SECOND, 1);
        MockUserSession session = new MockUserSession(TEST_USER, expire);
		String sessionId = userSessionService.registerNewSession(session);
		assertTrue(session.getExpiration().getTimeInMillis() - expire.getTimeInMillis() == 0);
        userSessionService.refreshSession(sessionId);
        assertTrue(session.getExpiration().getTimeInMillis() - expire.getTimeInMillis() > 0);
    }
}
