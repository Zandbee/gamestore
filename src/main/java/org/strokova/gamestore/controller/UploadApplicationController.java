package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.form.ApplicationForm;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.service.ApplicationPackageService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

/**
 * @author vstrokova, 12.09.2016.
 */
@Controller
@RequestMapping("/upload")
public class UploadApplicationController {

    private static final String PAGE_UPLOAD = "upload";

    @Autowired
    private ApplicationPackageService applicationPackageService;

    @RequestMapping(method = GET)
    @Secured("ROLE_DEVELOPER")
    public String showUploadApplicationPage(ApplicationForm applicationForm) {
        return PAGE_UPLOAD;
    }

    @RequestMapping(method = POST)
    @Secured("ROLE_DEVELOPER")
    public String uploadApplication(
            @Valid ApplicationForm applicationForm,
            Errors errors,
            Principal principal) {
        if (errors.hasErrors()) {
            return PAGE_UPLOAD;
        }

        applicationPackageService.saveUploadedApplication(
                applicationForm.getUserGivenName(),
                applicationForm.getDescription(),
                applicationForm.getCategory(),
                applicationForm.getFile(),
                principal.getName());
        return "upload"; // TODO: redirect
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.ALL);
    }
}
