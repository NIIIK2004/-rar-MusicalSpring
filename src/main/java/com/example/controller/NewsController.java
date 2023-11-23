package com.example.controller;

import com.example.model.Artist;
import com.example.model.News;
import com.example.model.PhotoArtists;
import com.example.repo.NewsRepo;
import jakarta.persistence.PostRemove;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NewsController {
    @Value("${upload.path}")
    private String uploadPath;

    private final NewsRepo newsRepo;

    @GetMapping(value = {"/news", "/Admin-News"})
    public String newsPage(Model model, HttpServletRequest request) {
        List<News> news = newsRepo.findAll();
        news = news.stream().sorted(Comparator.comparing(News::getId).reversed()).toList();

        model.addAttribute("newsList", news);

        if(request.getRequestURI().equals("/Admin-News")) {
            return "/admin/CreateOrDeleteNews";
        } else {
            return "News";
        }
    }

    @PostMapping("/news/save")
    public String save(@ModelAttribute("news") @Valid News news, @RequestParam("file") MultipartFile file, BindingResult result, Model model) {

        if (file.isEmpty()) {
            result.rejectValue("imageNews", "errorsNews" ,"Загрузите фотографию новости");
        }

        if (result.hasErrors()) {
            List<News> newsList = newsRepo.findAll();
            model.addAttribute("newsList", newsList);
            return "/Admin-News";
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
        return "redirect:/Admin-News";
    }

    @GetMapping("/news/delete/{id}")
    public String delete(@PathVariable Long id) {
        newsRepo.deleteById(id);
        return "redirect:/Admin-News";
    }
}
