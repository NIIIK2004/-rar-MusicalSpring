package com.example.repo;

import com.example.model.Artist;
import com.example.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepo extends JpaRepository<Playlist, Long> {
    List<Playlist> findByArtist(Artist artist);

}
