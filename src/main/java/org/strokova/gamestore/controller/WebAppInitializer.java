package org.strokova.gamestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.strokova.gamestore.configuration.ConfigRoot;
import org.strokova.gamestore.configuration.ConfigWeb;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        try {
        //Path uploadsPath = Paths.get(env.getProperty("upload.location.temp"));
        Path uploadsPath = Paths.get("D:/Temp/gamestore/tmp");
        if (Files.notExists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }
        registration.setMultipartConfig(
                new MultipartConfigElement(uploadsPath.toString())); // TODO: where is this folder? =|
        // TODO: move path to config ^^
        // TODO set restrictions: new MultipartConfigElement("/tmp/spittr/uploads", 2097152, 4194304, 0)); -
        // to limit files to no more than 2 MB, to limit the entire request to no more than 4 MB, and to write all files to disk.
        // set any instead of 0 to test temp folder
        } catch (IOException e) {
            // TODO
        }
    }
}
