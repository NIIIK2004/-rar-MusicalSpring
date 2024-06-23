package com.example.service;

import com.example.dto.TrackDTO;
import com.example.model.Track;
import com.example.repo.TrackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

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

//    public TrackDTO getPreviousTrack(Long trackId) throws IOException {
//        Optional<Track> currentTrackOptional = trackRepository.findById(trackId);
//
//        if (currentTrackOptional.isPresent()) {
//            Optional<Track> previousTrackOptional = trackRepository.findById(trackId - 1);
//            if (previousTrackOptional.isPresent()) {
//                return convertToDTO(previousTrackOptional.get());
//            } else {
//                throw new IOException("Предыдущий трек не найден");
//            }
//        } else {
//            throw new IOException("Трек не найден");
//        }
//    }
//
//    public TrackDTO getNextTrack(Long trackId) throws IOException {
//        Optional<Track> currentTrackOptional = trackRepository.findById(trackId);
//
//        if (currentTrackOptional.isPresent()) {
//            Optional<Track> previousTrackOptional = trackRepository.findById(trackId + 1);
//            if (previousTrackOptional.isPresent()) {
//                return convertToDTO(previousTrackOptional.get());
//            } else {
//                throw new IOException("Следующий трек не найден");
//            }
//        } else {
//            throw new IOException("Трек не найден");
//        }
//    }

    private TrackDTO convertToDTO(Track track) {
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setTitle(track.getTitle());
        trackDTO.setArtistsName(track.getArtists().getName());
        trackDTO.setCoverFilename(track.getCoverfilename());
        return trackDTO;
    }
}
















