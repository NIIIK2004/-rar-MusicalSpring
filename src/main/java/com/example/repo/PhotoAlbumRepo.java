package com.example.repo;

import com.example.model.PhotoArtists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoAlbumRepo extends JpaRepository<PhotoArtists, Long> {
    PhotoArtists save(PhotoArtists photo);
}
