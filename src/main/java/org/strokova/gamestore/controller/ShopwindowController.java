package org.strokova.gamestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author vstrokova, 09.09.2016.
 */
@Controller
@RequestMapping("/shop")
public class ShopwindowController {
    private static final String PAGE_SHOPWINDOW = "shopwindow";

    @RequestMapping(method = GET)
    public String showShopwindow(){
        return PAGE_SHOPWINDOW;
    }
}
