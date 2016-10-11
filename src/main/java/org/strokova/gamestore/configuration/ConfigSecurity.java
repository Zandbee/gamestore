package org.strokova.gamestore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.strokova.gamestore.model.Role;

import javax.sql.DataSource;

/**
 * @author vstrokova, 04.10.2016.
 */
@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    private static final String USERS_BY_USERNAME_QUERY = "select username, password, true from user where username=?";
    private static final String AUTHORITIES_BY_USERNAME_QUERY = "select username, concat('ROLE_', role) from user where username=?";

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(USERS_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY)
                .passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/login").permitAll()
                .and().rememberMe()
                    .key("gamestoreKey")
                .and().logout()
                .and().authorizeRequests()
                    .antMatchers("/resources/**").permitAll() // TODO: check if this is really needed (for unauthorized users on login and registration pages) - when everything is ready
                    .antMatchers("/registration").permitAll()
                    .antMatchers("/upload", "/upload/**").hasRole(Role.DEVELOPER.name())
                    .anyRequest().authenticated();
                //.and().requiresChannel()
                    //.antMatchers("/registration", "/login").requiresSecure(); // send user's credentials over HTTPS
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
