package com.example.repo;

import com.example.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepo extends JpaRepository<Artist, Long> {
}
