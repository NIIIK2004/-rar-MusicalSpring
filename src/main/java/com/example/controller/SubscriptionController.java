package com.example.controller;


import com.example.dao.SubscriptionDao;
import com.example.impl.UserImpl;
import com.example.model.Subscription;
import com.example.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class SubscriptionController {
    private SubscriptionDao subscriptionDao;
    @Autowired
    private UserImpl userImpl;

    @Autowired
    public SubscriptionController(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    @PostMapping("/subscribe")
    //Отправка данных на подписку артиста, со страницы подробной артиста
    public RedirectView subscribe(@RequestParam("userId") Long userId, @RequestParam("artistId") Long artistId) {
        subscriptionDao.subscription(userId, artistId);
        return new RedirectView("/artist/" + artistId + "/details");
    }

    @PostMapping("/unsubscribe")
    //Отправка данных на отписку артиста, со страницы подробной артиста
    public RedirectView unsubscribe(@RequestParam("userId") Long userId, @RequestParam("artistId") Long artistId, HttpServletRequest request) {
        subscriptionDao.unsubscription(userId, artistId);
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("/subscriptions")) {
            return new RedirectView("/subscriptions/" + userId);
        } else {
            return new RedirectView("/artist/" + artistId + "/details");
        }
    }

    @GetMapping("/subscriptions/{userId}")
    //Просмотр страницы всех подписок на которых подписан пользователь
    public String getSubscriptions(@PathVariable Long userId, Model model, Principal principal) {
        List<Subscription> subscribedArtists = subscriptionDao.getAllSubscriptionsByUser(userId);
        model.addAttribute("subscriptions", subscribedArtists);

        boolean isUserRegistered = principal != null;
        model.addAttribute("isUserRegistered", isUserRegistered);

        boolean isSubscribed = isUserRegistered && !subscribedArtists.isEmpty();
        model.addAttribute("isSubscribed", isSubscribed);

        if (isUserRegistered) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            model.addAttribute("user", user);
        }

        return "Subscriptions";
    }
}
