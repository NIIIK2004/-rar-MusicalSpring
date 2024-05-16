package com.example.dao;

import com.example.model.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistDao {
    Artist add(Artist artist);

    void delete(Long id);

    List<Artist> findAll();

    List<Artist> findByName(String name);

    Optional<Artist> findById(Long id);

    Artist editFields(Long id, String name, String description, String genre, String listeners, String country, String liking, String filename);
}
