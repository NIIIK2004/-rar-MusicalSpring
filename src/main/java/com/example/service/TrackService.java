package com.example.service;

import com.example.dto.TrackDTO;
import com.example.model.Track;
import com.example.repo.TrackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TrackService {

    @Autowired
    private TrackRepo trackRepository;

    public TrackDTO getTrackById(Long trackId) throws IOException {
        Optional<Track> trackOptional = trackRepository.findById(trackId);

        if (trackOptional.isPresent()) {
            Track track = trackOptional.get();
            TrackDTO trackDTO = new TrackDTO();
            trackDTO.setId(track.getId());
            trackDTO.setTitle(track.getTitle());
            trackDTO.setArtistsName(track.getArtists().getName());
            trackDTO.setCoverFilename(track.getCoverfilename());
            trackDTO.setYear(track.getReleaseyear());
            trackDTO.setGenre(track.getGenre());
            trackDTO.setLyric(track.getLyrics());
            return trackDTO;
        } else {
            throw new IOException("F");
        }
    }

    private TrackDTO convertToDTO(Track track) {
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setTitle(track.getTitle());
        trackDTO.setArtistsName(track.getArtists().getName());
        trackDTO.setCoverFilename(track.getCoverfilename());
        return trackDTO;
    }

    public TrackDTO getRandomTrack() {
        List<Track> allTracks = trackRepository.findAll();

        if(allTracks.isEmpty()) {
            return null;
        }
        int randomIndex = new Random().nextInt(allTracks.size());
        Track randomTrack = allTracks.get(randomIndex);
        TrackDTO trackDTO = convertToDTO(randomTrack);

        return trackDTO;
    }


}
















