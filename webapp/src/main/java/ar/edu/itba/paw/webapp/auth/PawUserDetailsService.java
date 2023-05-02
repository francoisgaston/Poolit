package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Component
public class PawUserDetailsService implements UserDetailsService {

    private final UserService us;

    @Autowired
    public PawUserDetailsService(final UserService us){
        this.us=us;
    }

    private enum AuthRoles{
        USER("ROLE_USER"),
        DRIVER("ROLE_DRIVER");
        private final String role;
        private AuthRoles(String role){
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user for email " + email));

        final Collection<GrantedAuthority> authorities = new HashSet<>();
        if(Objects.equals(user.getRole(), "DRIVER")){
            authorities.add(new SimpleGrantedAuthority(AuthRoles.DRIVER.role));
        } else {
            authorities.add(new SimpleGrantedAuthority(AuthRoles.USER.role));
        }

        return new AuthUser(email, user.getPassword(), authorities);
    }


    public void update(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final Collection<GrantedAuthority> authorities = new HashSet<>();
        if(Objects.equals(user.getRole(), "USER")){
            authorities.add(new SimpleGrantedAuthority(AuthRoles.DRIVER.role));
        }else {
            authorities.add(new SimpleGrantedAuthority(AuthRoles.USER.role));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}