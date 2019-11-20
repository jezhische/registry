package com.tagsoft.registry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping(value = "/swagger")
    public String getSwaggerPage() {
        return "redirect:swagger-ui.html";
    }
}
