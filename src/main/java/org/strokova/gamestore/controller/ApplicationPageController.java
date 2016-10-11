package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.exception.ApplicationFileNotFoundException;
import org.strokova.gamestore.exception.FileTransferException;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.service.ApplicationService;
import org.strokova.gamestore.util.PathsManager;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * author: Veronika, 9/29/2016.
 */
@Controller
@RequestMapping("/{applicationId}")
public class ApplicationPageController {

    private static final String PAGE_APPLICATION = "applicationPage";
    private static final String MIME_ZIP = "application/zip";

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = GET)
    public String viewApplication(
            @PathVariable int applicationId,
            Model model) {
        Application application = applicationService.getApplicationById(applicationId);
        model.addAttribute("app", application);
        return PAGE_APPLICATION;
    }

    @RequestMapping(value = "/download", method = GET)
    public void downloadApplicationFile(
            HttpServletResponse response,
            @PathVariable int applicationId) {
        Application application = applicationService.getApplicationById(applicationId);

        String filePath = application.getFilePath();
        File applicationFile = new File(PathsManager.UPLOADS_DIR + File.separator + filePath);

        if (Files.notExists(applicationFile.toPath())) {
            throw new ApplicationFileNotFoundException("Application file was not found at path: " + applicationFile);
        }

        try {
            response.setContentType(MIME_ZIP);
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(applicationFile.getName(), "UTF-8"));
            response.setContentLength((int) applicationFile.length());
            FileCopyUtils.copy(new BufferedInputStream(new FileInputStream(applicationFile)),
                    response.getOutputStream());
        } catch (IOException e) {
            throw new FileTransferException("Cannot send " + applicationFile, e);
        }

        applicationRepository.incrementDownloadNumber(applicationId);
    }
}
