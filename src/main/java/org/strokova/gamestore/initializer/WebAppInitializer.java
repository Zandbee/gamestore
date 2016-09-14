package org.strokova.gamestore.initializer;

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

    private static final String UPLOAD_TEMP_PATH = "D:/Temp/gamestore/tmp"; // TODO: move to config?
    private static final int MAX_FILE_SIZE = 2097152; // 2 MB
    private static final int MAX_REQUEST_SIZE = 4194304; // 4 MB
    private static final int FILE_SIZE_THRESHOLD = 0;

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
        Path uploadsPath = Paths.get(UPLOAD_TEMP_PATH);
        if (Files.notExists(uploadsPath)) {
            Files.createDirectories(uploadsPath);
        }
        registration.setMultipartConfig(
                new MultipartConfigElement(uploadsPath.toString(), MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
        } catch (IOException e) {
            // TODO
        }
    }
}
