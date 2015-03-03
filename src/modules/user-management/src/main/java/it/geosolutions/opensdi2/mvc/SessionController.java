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
package it.geosolutions.opensdi2.mvc;

import it.geosolutions.opensdi2.session.UserSession;
import it.geosolutions.opensdi2.session.UserSessionService;
import it.geosolutions.opensdi2.session.impl.UserSessionImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/session")
public class SessionController {

    @Autowired
    @Resource(name="userSessionService")
    UserSessionService userSessionService;
    
    private SecurityContext securityContext;
    
    private static SimpleDateFormat expireParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    
    /**
     * Gets the User object associated to the given sessionId (if it exists).
     * 
     * @param sessionId
     * @return
     */
    @RequestMapping(value = "/user/{sessionId}", method = RequestMethod.GET)
	public @ResponseBody
	UserDetails getUser(
			@PathVariable String sessionId,
			@RequestParam(required = false, value = "refresh", defaultValue = "true") boolean refresh) {
        UserDetails details = userSessionService.getUserData(sessionId);
        if(details != null && refresh) {
        	userSessionService.refreshSession(sessionId);
        }
        return details;
    }
    
    /**
     * Gets the username associated to the given sessionId (if it exists).
     * 
     * @param sessionId
     * @return
     */
    @RequestMapping(value = "/username/{sessionId}", method = RequestMethod.GET)
    public @ResponseBody String getUserName(@PathVariable String sessionId,
			@RequestParam(required = false, value = "refresh", defaultValue = "true") boolean refresh) {
        UserDetails userData = userSessionService.getUserData(sessionId);
        if(userData != null) {
        	if(refresh) {
        		userSessionService.refreshSession(sessionId);
        	}
            return userData.getUsername();
        }
        return null;
    }
    
    private Calendar getExpiration(String expires)
            throws ParseException {
        if(!"".equals(expires)) {
            return toCalendar(expires);
        }
        return null;
    }
    
    /**
     * Creates a new session for the User in SecurityContext.
     * 
     * @return
     * @throws ParseException 
     */
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @PreAuthorize("!hasRole('ROLE_ANONYMOUS')")
    public @ResponseBody String createSession(@RequestParam(defaultValue="",required=false) String expires) throws ParseException {
        Object user = getSecurityContext().getAuthentication().getPrincipal();
        if(user != null) {
            Calendar expiration = getExpiration(expires);
            UserSession session = null;
            if(user instanceof UserDetails) {
                 session = new UserSessionImpl(null,(UserDetails) user, expiration);
            } else {
                User userData = new User(user.toString(), "", getSecurityContext().getAuthentication().getAuthorities());
                session = new UserSessionImpl(null, userData, expiration);
            }
            return userSessionService.registerNewSession(session);
        }
         
        return null;
    }
    
    
    
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    private SecurityContext getSecurityContext() {
        if(securityContext != null) {
            return securityContext;
        }
        return SecurityContextHolder.getContext();
    }

    /**
     * Removes the given session.
     * 
     * @return
     */
    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    //@PreAuthorize("(!hasRole('ROLE_ANONYMOUS') and @userSessionService.isOwner(#sessionId,principal)) or hasRole('ROLE_ADMIN')")
    public void removeSession(@PathVariable String sessionId) {
       userSessionService.removeSession(sessionId);
    }
    
    /**
     * Removes all sessions.
     * 
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void clear() {
       userSessionService.removeAllSessions();
    }
    
    /** Transform Calendar to ISO 8601 string. */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    
    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = expireParser.parse(s);
        calendar.setTime(date);
        return calendar;
    }
}
