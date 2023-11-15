package com.example.dao;

import com.example.model.Subscription;

import java.util.List;

public interface SubscriptionDao {
    List<Subscription> getAllSubscriptionsByUser(Long userId);
    List<Subscription> getAllSubscriptionsByArtist(Long artistId);
    void subscription(Long userId, Long artistId);
    void unsubscription(Long userId, Long artistId);

    boolean checkSubscription(Long userId, Long artistId);

}
