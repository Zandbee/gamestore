package org.strokova.gamestore.controller;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.strokova.gamestore.configuration.ConfigRoot;
import org.strokova.gamestore.configuration.ConfigWeb;

/**
 * @author vstrokova, 06.09.2016.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {ConfigRoot.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {ConfigWeb.class};
    }
}
