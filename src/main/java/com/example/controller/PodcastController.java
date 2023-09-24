package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PodcastController {
    @GetMapping("/podcasts")
    public String PodcastMainPage(Model model) {
        return "Podcasts";
    }
}
