package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.form.ApplicationForm;
import org.strokova.gamestore.model.Application;
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
    private static final String PATH_VAR_APPLICATION_ID = "applicationId";

    private final ApplicationPackageService applicationPackageService;

    @Autowired
    public UploadApplicationController(ApplicationPackageService applicationPackageService) {
        this.applicationPackageService = applicationPackageService;
    }

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
            Principal principal,
            Model model) {
        if (errors.hasErrors()) {
            return PAGE_UPLOAD;
        }

        Application application = applicationPackageService.saveUploadedApplication(applicationForm, principal.getName());

        model.asMap().clear();
        model.addAttribute(PATH_VAR_APPLICATION_ID, application.getId());
        return "redirect:/{" + PATH_VAR_APPLICATION_ID + "}";
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.values());
    }
}
