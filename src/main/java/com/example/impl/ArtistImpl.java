package com.example.impl;

import com.example.dao.ArtistDao;
import com.example.model.Artist;
import com.example.repo.ArtistRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistImpl implements ArtistDao {
    private final ArtistRepo artistRepo;

    public ArtistImpl(ArtistRepo artistRepo) {
        this.artistRepo = artistRepo;
    }

    @Override
    public Artist add(Artist artist) {
        return this.artistRepo.save(artist);
    }

    @Override
    public void delete(Long id) {
        this.artistRepo.deleteById(id);
    }

    @Override
    public List<Artist> findAll() {
        return artistRepo.findAll();
    }

    @Override
    public List<Artist> findByName(String name) {
        return artistRepo.findByName(name);
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return this.artistRepo.findById(id);
    }

    @Override
    public Artist editFields(Long id, String name, String description, String genre, String listeners, String filename) {
        Optional<Artist> optionalArtist = artistRepo.findById(id);

        if (optionalArtist.isPresent()) {
            Artist existingArtist = optionalArtist.get();
            existingArtist.setName(name);
            existingArtist.setDescription(description);
            existingArtist.setGenre(genre);
            existingArtist.setListeners(listeners);
            existingArtist.setFilename(filename);
            return artistRepo.save(existingArtist);
        }
        throw new IllegalArgumentException("Невалид броу артист " + id);
    }
}
