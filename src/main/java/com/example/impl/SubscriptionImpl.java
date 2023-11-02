package com.example.impl;

import com.example.dao.SubscriptionDao;
import com.example.model.Artist;
import com.example.model.Subscription;
import com.example.model.User;
import com.example.repo.ArtistRepo;
import com.example.repo.SubscriptionRepo;
import com.example.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionImpl implements SubscriptionDao {
    private final SubscriptionRepo subscriptionRepo;
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;

    @Override
    public List<Subscription> getAllSubscriptionsByUser(Long userId) {
        return subscriptionRepo.findByUserId(userId);
    }

    @Override
    public List<Subscription> getAllSubscriptionsByArtist(Long artistId) {
        return subscriptionRepo.findByArtistId(artistId);
    }

    @Override
    public void subscription(Long userId, Long artistId) {
        Optional<User> user = userRepo.findById(userId);
        Optional<Artist> artist = artistRepo.findById(artistId);

        if (user.isPresent() && artist.isPresent()) {
            List<Subscription> existingSubscription = subscriptionRepo.findByUserIdAndArtistId(userId, artistId);
            if (existingSubscription.isEmpty()) {
                Subscription subscription = new Subscription(user.get(), artist.get());
                subscriptionRepo.save(subscription);
            }
        }

    }

    @Override
    public void unsubscription(Long userId, Long artistId) {
        List<Subscription> existingSubscription = subscriptionRepo.findByUserIdAndArtistId(userId, artistId);
        if (!existingSubscription.isEmpty()) {
            Subscription subscription = existingSubscription.get(0);
            subscriptionRepo.delete(subscription);
        }
    }

    @Override
    public boolean checkSubscription(Long userId, Long artistId) {
        List<Subscription> existingSubscription = subscriptionRepo.findByUserIdAndArtistId(userId, artistId);
        return !existingSubscription.isEmpty();
    }
}
