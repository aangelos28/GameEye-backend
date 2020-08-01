package edu.odu.cs411yellow.gameeyebackend.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/public/*").permitAll()
                .mvcMatchers("/private/*").authenticated()
                .mvcMatchers("/private-admin/*").hasAuthority("admin")
                .and()
                .cors()
                .and()
                .addFilterBefore(firebaseIdTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    public FirebaseIdTokenFilter firebaseIdTokenFilterBean() throws Exception {
        return new FirebaseIdTokenFilter();
    }
}
