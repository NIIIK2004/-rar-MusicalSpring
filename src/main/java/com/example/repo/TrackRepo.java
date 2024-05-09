package com.example.repo;

import com.example.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackRepo extends JpaRepository<Track, Long> {
    Optional<Track> findTopByArtistsIdOrderByCoverfilename(Long artistId);
    @Query("SELECT a FROM Track a WHERE a.title LIKE %:name%")
    List<Track> findByName(@Param("name") String name);
    long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}
