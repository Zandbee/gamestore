package org.strokova.gamestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.strokova.gamestore.model.Application;
import org.strokova.gamestore.model.Category;
import org.strokova.gamestore.repository.ApplicationRepository;
import org.strokova.gamestore.service.ApplicationService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author vstrokova, 09.09.2016.
 */
@Controller
@RequestMapping({"/", "/shopwindow"})
public class ShopwindowController {
    private static final String PAGE_SHOPWINDOW = "shopwindow";

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = GET)
    public String showShopwindow() {
        return PAGE_SHOPWINDOW;
    }

    @ModelAttribute("allCategories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.ALL);
    }

    @ModelAttribute("pageApplications")
    public Page<Application> findApplicationPage(
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "category", defaultValue = "") String category) {
        return applicationService.findApplicationsPage(pageNum, category);
    }

    @ModelAttribute("pageCount")
    public int getPageCount(
            @RequestParam(value = "category", defaultValue = "") String category) {
        return applicationService.getPageCount(category);
    }

    @ModelAttribute("popularApplications")
    public Page<Application> findPopularApplications() {
        return applicationService.findMostPopularApplications();
    }
}
