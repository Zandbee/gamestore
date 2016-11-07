package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.Role;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.service.RegistrationService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author vstrokova, 07.09.2016.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final String PAGE_REGISTRATION = "registration";
    private static final String PAGE_SHOPWINDOW = "shopwindow";

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(method = GET)
    public String showRegistrationPage(User user) {
        return PAGE_REGISTRATION;
    }

    @RequestMapping(method = POST)
    public String processRegistration(
            @Valid User user,
            Errors errors,
            Model model) {
        if (errors.hasErrors()) {
            return PAGE_REGISTRATION;
        }

        registrationService.saveAndAuthenticateUser(user);

        model.asMap().clear();
        return "redirect: " + PAGE_SHOPWINDOW;
    }

    @ModelAttribute("allRoles")
    public List<Role> populateRoles() {
        return Arrays.asList(Role.values());
    }
}
