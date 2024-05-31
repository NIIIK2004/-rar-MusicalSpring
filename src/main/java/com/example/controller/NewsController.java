package com.example.controller;

import com.example.model.News;
import com.example.repo.NewsRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NewsController {
    @Value("${upload.path}")
    private String uploadPath;

    private final NewsRepo newsRepo;

    @GetMapping(value = {"/news", "/Admin-News"})
    //Просмотр страницы Новости
    public String newsPage(Model model, HttpServletRequest request) {
        List<News> news = newsRepo.findAll();
        news = news.stream().sorted(Comparator.comparing(News::getId).reversed()).toList();

        model.addAttribute("newsList", news);
        model.addAttribute("pageTitle", "Новости");

        if (request.getRequestURI().equals("/Admin-News")) {
            return "/admin/CreateOrDeleteNews";
        } else {
            return "News";
        }
    }

    @PostMapping("/news/save")
    //Создание новости от имени Админа
    public String save(@ModelAttribute("news") @Valid News news, @RequestParam("file") MultipartFile file, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        List<News> newsList = newsRepo.findAll();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorImg", "Загрузите фотографию новости");
            return "redirect:/Admin-News";
        }

        if (result.hasErrors()) {
            model.addAttribute("newsList", newsList);
            return "redirect:/Admin-News";
        }

        try {
            String filename = UUID.randomUUID() + ".jpg";
            file.transferTo(new File(uploadPath + "/" + filename));
            news.setImageNews(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        news.setDate(LocalDateTime.now());

        newsRepo.save(news);
        redirectAttributes.addFlashAttribute("successAdd", "Новость была добавлена");
        return "redirect:/Admin-News";
    }

    @GetMapping("/news/delete/{id}")
    //Удаление новости
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        News news = newsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Инвалид айди друк"));
        String imagesNews = news.getImageNews();

        if (imagesNews != null) {
            String imagePath = uploadPath + "/" + imagesNews;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }

        newsRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("successDelete", "Новость была удалена");
        return "redirect:/Admin-News";
    }
}
