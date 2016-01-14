package it.geosolutions.opensdi2.security;

import com.sun.jersey.api.client.ClientHandlerException;
import it.geosolutions.geostore.core.model.User;
import it.geosolutions.geostore.services.rest.AdministratorGeoStoreClient;
import it.geosolutions.geostore.services.security.GeoStoreAuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class GeoStoreAuthenticationProviderWithUserPrincipal extends GeoStoreAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) {
        String pw = (String) authentication.getCredentials();
        String us = (String) authentication.getPrincipal();
        // We use the credentials for all the session in the GeoStore client

        AdministratorGeoStoreClient geoStoreClient = new AdministratorGeoStoreClient();
        geoStoreClient.setUsername(us);
        geoStoreClient.setPassword(pw);
        geoStoreClient.setGeostoreRestUrl(super.getGeoStoreRestURL());

        User user = null;
        try {
            user = geoStoreClient.getUserDetails();
        } catch (ClientHandlerException che) {
            throw new UsernameNotFoundException(GEOSTORE_UNAVAILABLE);
        } catch (Exception e) {
            // user not found generic response.
            user = null;
        }

        if (user != null) {
            String role = user.getRole().toString();
            if (!roleAllowed(role)) {
                throw new BadCredentialsException(UNAUTHORIZED_MSG);
            }
//				return null;
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new GrantedAuthorityImpl("ROLE_" + role));
            Authentication a = new UsernamePasswordAuthenticationToken(user, pw, authorities);
            // a.setAuthenticated(true);
            return a;
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
        }
    }

    private boolean roleAllowed(String role) {
        for (String allowed : super.getAllowedRoles()) {
            if (allowed != null) {
                if (allowed.equals(role))
                    return true;
            }
        }
        return false;
    }
}
