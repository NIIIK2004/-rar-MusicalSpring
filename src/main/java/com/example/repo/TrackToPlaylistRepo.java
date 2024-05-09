package com.example.repo;

import com.example.model.PlaylistContent;
import com.example.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackToPlaylistRepo extends JpaRepository<PlaylistContent, Long> {
}
