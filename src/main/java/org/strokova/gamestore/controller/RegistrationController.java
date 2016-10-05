package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.Role;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = GET)
    public String showRegistrationPage(User user) {
        return PAGE_REGISTRATION;
    }

    @Transactional // TODO: need this?
    @RequestMapping(method = POST)
    public String processRegistration(User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        model.asMap().clear();
        return "redirect: " + PAGE_SHOPWINDOW;
    }

    @ModelAttribute("allRoles")
    public List<Role> populateRoles() {
        return Arrays.asList(Role.ALL);
    }
}
