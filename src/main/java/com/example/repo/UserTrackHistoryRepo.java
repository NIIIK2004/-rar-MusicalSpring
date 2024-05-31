package com.example.repo;

import com.example.model.Track;
import com.example.model.User;
import com.example.model.UserTrackHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTrackHistoryRepo extends JpaRepository<UserTrackHistory, Long> {
    List<UserTrackHistory> findByUserAndTrack(User user, Track track);
    @Transactional
    void deleteByTrack(Track track);
    List<UserTrackHistory> findByUserId(Long userId);
    @Query("SELECT SUM(uth.listenCount) FROM UserTrackHistory uth WHERE uth.user = :user")
    Integer getSumOfListenCountByUser(@Param("user") User user);
    default int getSumOfListenCountByUserDividedByTwo(User user) {
        Integer result = getSumOfListenCountByUser(user);
        return result != null ? (int)Math.floor(result / 2.0) : 0;
    }

    List<UserTrackHistory> findByTrackId(Long id);
}
