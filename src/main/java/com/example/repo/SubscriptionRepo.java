package com.example.repo;

import com.example.model.Subscription;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByArtistId(Long artistId);
    List<Subscription> findByUserIdAndArtistId(Long userId, Long artistId);
    List<Subscription> getAllSubscriptionsByUser(User user);
    List<Subscription> findByUser(User user);

}
