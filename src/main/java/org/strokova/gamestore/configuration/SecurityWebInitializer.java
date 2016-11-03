package org.strokova.gamestore.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author vstrokova, 04.10.2016.
 */

/**
 * Registers the DelegatingFilterProxy to use the springSecurityFilterChain before any other registered Filter
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
}
