package com.example.controller;


import com.example.dao.SubscriptionDao;
import com.example.model.Subscription;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequiredArgsConstructor

public class SubscriptionController {
    private SubscriptionDao subscriptionDao;

    @Autowired
    public SubscriptionController(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    @PostMapping("/subscribe")
    public RedirectView subscribe(@RequestParam("userId") Long userId, @RequestParam("artistId") Long artistId) {
        subscriptionDao.subscription(userId, artistId);
        return new RedirectView("/artist/" + artistId + "/details");
    }

    @PostMapping("/unsubscribe")
    public RedirectView unsubscribe(@RequestParam("userId") Long userId, @RequestParam("artistId") Long artistId) {
        subscriptionDao.unsubscription(userId, artistId);
        return new RedirectView("/artist/" + artistId + "/details");
    }

    @GetMapping("/subscriptions/{userId}")
    public String getSubscriptions(@PathVariable Long userId, Model model) {
        List<Subscription> subscribedArtists = subscriptionDao.getAllSubscriptionsByUser(userId);
        model.addAttribute("subscriptions", subscribedArtists);
        return "Subscriptions";
    }
}
