package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.util.PathsManager;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

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
        model.addAttribute("app", applicationRepository.findOne(applicationId));
        return "applicationPage";
    }

    @RequestMapping(value = "/download", method = GET)
    public void downloadApplicationFile(
            HttpServletResponse response,
            @PathVariable int applicationId) {
        Application application = applicationRepository.findOne(applicationId);
        String filePath = application.getFilePath();

        File applicationFile = new File(PathsManager.UPLOADS_DIR + File.separator + filePath);

        if (Files.notExists(applicationFile.toPath())) {
            // TODO
            return;
        }

        String mimeType = "application/zip";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + applicationFile.getName());
        response.setContentLength((int) applicationFile.length());
        try {
            FileCopyUtils.copy(new BufferedInputStream(new FileInputStream(applicationFile)),
                    response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
    }
}
