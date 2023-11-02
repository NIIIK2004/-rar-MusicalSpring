package com.example.impl;

import com.example.dao.TrackDao;
import com.example.model.Artist;
import com.example.model.Track;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TrackImpl implements TrackDao {

    private final TrackRepo trackRepo;
    private ArtistRepo artistRepo;

    @Override
    public Track add(Track track) {
        return this.trackRepo.save(track);
    }

    @Override
    public void delete(Long id) {
        this.trackRepo.deleteById(id);
    }

    @Override
    public List<Track> findAll() {
        return this.trackRepo.findAll();
    }

    @Override
    public List<Track> findByName(String name) {
        return trackRepo.findByName(name);
    }

    @Override
    public Optional<Track> findById(Long id) {
        return this.trackRepo.findById(id);
    }

    public Track findLatestTrackByArtistId(Long artistId) {
        Optional<Track> optionalTrack = trackRepo.findTopByArtistsIdOrderByCoverfilename(artistId);
        return optionalTrack.orElse(null);
    }

    @Override
    public Track editFileds(Long id, String title, String description, String lyrics, String genre, String releaseyear, String audiofilename, String coverfilename, String clipfilename, Artist artist) {
        Optional<Track> optionalTrack = trackRepo.findById(id);

        if (optionalTrack.isPresent()) {
            Track existingTrack = optionalTrack.get();
            existingTrack.setTitle(title);
            existingTrack.setDescription(description);
            existingTrack.setLyrics(lyrics);
            existingTrack.setGenre(genre);
            existingTrack.setReleaseyear(releaseyear);
            existingTrack.setAudiofilename(audiofilename);
            existingTrack.setCoverfilename(coverfilename);

            if (artist != null) {
                Artist existingArtist = artistRepo.findById(artist.getId()).orElse(null);
                existingTrack.setArtists(existingArtist);
            }

            return trackRepo.save(existingTrack);
        }

        throw new IllegalArgumentException("Невалидный трек: " + id);
    }
}
