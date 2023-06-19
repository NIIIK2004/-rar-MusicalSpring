package com.example.repo;

import com.example.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepo extends JpaRepository<Track, Long> {
    Optional<Track> findTopByArtistsIdOrderByCoverfilename(Long artistId);

}
