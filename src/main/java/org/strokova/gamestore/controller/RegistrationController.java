package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author vstrokova, 07.09.2016.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final String PAGE_REGISTRATION_NAME = "registration";

    @RequestMapping(method = GET)
    public String registration() {
        return PAGE_REGISTRATION_NAME;
    }
}
