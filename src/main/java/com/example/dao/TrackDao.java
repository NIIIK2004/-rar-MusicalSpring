package com.example.dao;

import com.example.model.Artist;
import com.example.model.Track;

import java.util.List;
import java.util.Optional;

public interface TrackDao {
    Track add(Track track);

    void delete(Long id);

    List<Track> findAll();

    Optional<Track> findById(Long id);

    List<Track> findByName(String name);

    Track findLatestTrackByArtistId(Long artistId);

    Track editFileds(Long id,
                     String title,
                     String lyrics,
                     String genre,
                     String releaseyear,
                     String audiofilename,
                     String coverfilename,
                     Artist artists);
}
