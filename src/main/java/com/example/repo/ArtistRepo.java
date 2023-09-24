package com.example.repo;

import com.example.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistRepo extends JpaRepository<Artist, Long> {
}
