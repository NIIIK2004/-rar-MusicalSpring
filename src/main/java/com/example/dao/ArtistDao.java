package com.example.dao;

import com.example.model.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistDao {
    public Artist add(Artist artist);

    public void delete(Long id);

    public List<Artist> findAll();

    public List<Artist> findByName(String name);

    public Optional<Artist> findById(Long id);

    Artist editFields(Long id, String name, String description, String genre, String listeners, String country, String liking, String filename);
}
