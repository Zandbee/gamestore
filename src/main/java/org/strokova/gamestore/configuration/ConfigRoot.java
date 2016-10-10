package org.strokova.gamestore.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.strokova.gamestore.Root;

/**
 * @author vstrokova, 06.09.2016.
 */
@Configuration
@ComponentScan(basePackageClasses = Root.class,
        excludeFilters = {@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class ConfigRoot {
}
