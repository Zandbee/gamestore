package org.strokova.gamestore.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author vstrokova, 05.10.2016.
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ConfigMethodSecurity extends GlobalMethodSecurityConfiguration {
}
