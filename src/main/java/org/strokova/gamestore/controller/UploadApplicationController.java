package org.strokova.gamestore.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.strokova.gamestore.model.Application;

/**
 * @author vstrokova, 12.09.2016.
 */
@Controller
@RequestMapping("/upload")
public class UploadApplicationController {

    @RequestMapping(method = GET)
    public String showUploadApplicationPage() {
        return "upload";
    }

    @RequestMapping(method = POST)
    public String uploadApplication() {
        return "upload";
    }
}
