package edu.odu.cs411yellow.gameeyebackend.security;

import edu.odu.cs411yellow.gameeyebackend.config.Secrets;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String audience = Secrets.Firebase.getAudience();

    private final String issuer = Secrets.Firebase.getIssuerUri();

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/public/*").permitAll()
                .mvcMatchers("/private/*").authenticated()
                .mvcMatchers("/private-admin/*").hasAuthority("SCOPE_admin:all")
                .and()
                .cors()
                .and()
                .addFilterBefore(firebaseIdTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    public FirebaseIdTokenFilter firebaseIdTokenFilterBean() throws Exception {
        return new FirebaseIdTokenFilter();
    }
}
