package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.repository.UserRepository;

/**
 * @author vstrokova, 07.09.2016.
 */
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private static final String PAGE_REGISTRATION_NAME = "registration";

    private UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = GET)
    public String showRegistrationPage() {
        return PAGE_REGISTRATION_NAME;
    }

    @Transactional
    @RequestMapping(method = POST)
    public String processRegistration(User user) {
        userRepository.save(user);
        return "index"; // TODO: return "redirect: mainPage"
    }
}
