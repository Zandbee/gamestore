package org.strokova.gamestore.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.strokova.gamestore.exception.InternalErrorException;

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

    private static final int MAX_FILE_SIZE = 2097152; // 2 MB
    private static final int MAX_REQUEST_SIZE = 4194304; // 4 MB
    private static final int FILE_SIZE_THRESHOLD = 0;

    private static final String DEFAULT_TEMP_DIR_PROPERTY = "java.io.tmpdir";
    private static final String MULTIPART_TEMP_DIR_NAME = "/gamestore";

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ConfigRoot.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ConfigWeb.class};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        try {
            Path multipartUploadsPath = Paths.get(System.getProperty(DEFAULT_TEMP_DIR_PROPERTY) + MULTIPART_TEMP_DIR_NAME);
            if (Files.notExists(multipartUploadsPath)) {
                Files.createDirectories(multipartUploadsPath);
            }
            registration.setMultipartConfig(
                    new MultipartConfigElement(multipartUploadsPath.toString(), MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
        } catch (IOException e) {
            throw new InternalErrorException("Cannot create directory");
        }
    }
}
