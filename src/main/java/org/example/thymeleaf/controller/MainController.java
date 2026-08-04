package org.example.thymeleaf.controller;

import lombok.RequiredArgsConstructor;
import org.example.thymeleaf.config.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final AppProperties appProperties;
//    private final String msg;
//
//    public MainController(
//            AppProperties appProperties,
//            @Value("${app.message}") String msg) {
//        this.appProperties = appProperties;
//        this.msg = msg;
//    }

    @Value("${app.message}")
    private String msg; // 일종의 필드 주입

    @GetMapping
    public String index(Model model) {
        model.addAttribute("msg", appProperties.message());
        model.addAttribute("msg2", msg);
        return "index";
    }
}