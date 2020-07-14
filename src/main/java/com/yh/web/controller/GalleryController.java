package com.yh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/galleries")
@Controller
public class GalleryController {

    @GetMapping(path = "")
    @ResponseBody
    public String index(){
        return "언제 다하지..";
    }
}
