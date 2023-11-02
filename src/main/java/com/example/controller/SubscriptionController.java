package com.example.controller;


import com.example.dao.SubscriptionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
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
}
