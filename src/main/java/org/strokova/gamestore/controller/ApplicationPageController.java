package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.repository.ApplicationRepository;

/**
 * author: Veronika, 9/29/2016.
 */
@Controller
public class ApplicationPageController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(value="/{applicationId}", method = GET)
    public String viewApplication(
            @PathVariable int applicationId,
            Model model) {
        model.addAttribute("app", applicationRepository.findOne(applicationId));
        return "applicationPage";
    }
}
