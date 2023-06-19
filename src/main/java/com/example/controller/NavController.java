package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavController {
    @GetMapping("/subscribe")
    public String subscribePage() {
        return "subscribe";
    }

    @GetMapping("/project")
    public String projectPage() {
        return "project";
    }

    @GetMapping("/collection")
    public String collectionPage() {
        return "collection";
    }
}
