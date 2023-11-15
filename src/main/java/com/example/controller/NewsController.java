package com.example.controller;

import com.example.model.News;
import com.example.repo.NewsRepo;
import jakarta.persistence.PostRemove;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NewsController {
    @Value("${upload.path}")
    private String uploadPath;

    private final NewsRepo newsRepo;

    @GetMapping("/news")
    public String newsPage(Model model) {
        List<News> news = newsRepo.findAll();
        model.addAttribute("newsList", news);
        return "News";
    }

    @PostMapping("/news/save")
    public String save(@ModelAttribute("news") @Valid News news, @RequestParam("file") MultipartFile file, BindingResult result, Model model) {

        if (file.isEmpty()) {
            result.rejectValue("imageNews", "errorsNews" ,"Загрузите фотографию новости");
        }

        if (result.hasErrors()) {
            List<News> newsList = newsRepo.findAll();
            model.addAttribute("newsList", newsList);
            return "News";
        }


        try {
            String filename = UUID.randomUUID().toString() + ".jpg";
            file.transferTo(new File(uploadPath + "/" + filename));
            news.setImageNews(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }



        news.setDate(LocalDateTime.now());

        newsRepo.save(news);
        return "redirect:/news";
    }

    @GetMapping("/news/delete/{id}")
    public String delete(@PathVariable Long id) {
        newsRepo.deleteById(id);
        return "redirect:/news";
    }
}
