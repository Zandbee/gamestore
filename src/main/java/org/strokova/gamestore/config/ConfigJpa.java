package org.strokova.gamestore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author vstrokova, 05.09.2016.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.strokova.gamestore.repository")
public class ConfigJpa {
}
