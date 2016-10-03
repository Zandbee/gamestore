package org.strokova.gamestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.service.ApplicationService;
import org.strokova.gamestore.service.ApplicationServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author vstrokova, 09.09.2016.
 */
@Controller
@RequestMapping("/")
public class ShopwindowController {
    private static final String PAGE_SHOPWINDOW = "shopwindow";

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = GET)
    public String showShopwindow(){
        return PAGE_SHOPWINDOW;
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.ALL);
    }

    @ModelAttribute("allApplications")
    public List<Application> findApplications() {
        return applicationRepository.findAll();
    }

    @ModelAttribute("popularApplications")
    public Page<Application> findPopularApplications() {return applicationService.findMostPopularApplications();}
}
