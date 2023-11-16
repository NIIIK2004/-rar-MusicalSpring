package com.example.repo;

import com.example.model.PhotoArtists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoToAlbumRepo extends JpaRepository<PhotoArtists, Long> {
    void delete(PhotoArtists photoArtists);
}
