package org.strokova.gamestore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * @author vstrokova, 04.10.2016.
 */
@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    private static final String USERS_BY_USERNAME_QUERY = "select username, password, true from user where username=?";
    private static final String AUTHORITIES_BY_USERNAME_QUERY = "select concat('role_', u.role) as authority";

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery(USERS_BY_USERNAME_QUERY)
        .authoritiesByUsernameQuery();
    }
}
