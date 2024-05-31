package com.example.repo;

import com.example.model.PlaylistContent;
import com.example.model.Track;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistContentsRepo extends JpaRepository<PlaylistContent, Long> {

    List<PlaylistContent> findByTrackId(Long id);
    @Transactional
    void deleteByTrack(Track track);
}
