package com.pkkl.BreadMeUp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping(
            value = "/"
    )
    public String swaggerUi() {
        return "redirect:/swagger-ui.html";
    }
}