package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.exception.ApplicationFileNotFoundException;
import org.strokova.gamestore.exception.ApplicationNotFoundException;
import org.strokova.gamestore.exception.FileTransferException;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.service.ApplicationService;
import org.strokova.gamestore.util.PathsManager;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * author: Veronika, 9/29/2016.
 */
@Controller
@RequestMapping("/{applicationId}")
public class ApplicationPageController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(method = GET)
    public String viewApplication(
            @PathVariable int applicationId,
            Model model) {
        Application application = applicationRepository.findOne(applicationId);

        if (application == null) {
            throw new ApplicationNotFoundException("Application with ID = " + applicationId + " was not found");
        }

        model.addAttribute("app", application);
        return "applicationPage";
    }

    // TODO: need transaction here? move to service?
    @RequestMapping(value = "/download", method = GET)
    public void downloadApplicationFile(
            HttpServletResponse response,
            @PathVariable int applicationId) {
        Application application = applicationRepository.findOne(applicationId);

        if (application == null) {
            throw new ApplicationNotFoundException("Application with ID = " + applicationId + " was not found");
        }

        String filePath = application.getFilePath();
        File applicationFile = new File(PathsManager.UPLOADS_DIR + File.separator + filePath);

        if (Files.notExists(applicationFile.toPath())) {
            throw new ApplicationFileNotFoundException("Application file was not found at path: " + applicationFile);
        }

        String mimeType = "application/zip";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + applicationFile.getName());
        response.setContentLength((int) applicationFile.length());
        try {
            FileCopyUtils.copy(new BufferedInputStream(new FileInputStream(applicationFile)),
                    response.getOutputStream());
        } catch (IOException e) {
            throw new FileTransferException("Cannot send " + applicationFile, e);
        }

        applicationRepository.incrementDownloadNumber(applicationId);
    }
}
