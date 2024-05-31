package com.example.service;

import com.example.model.Artist;
import com.example.model.Playlist;
import com.example.repo.PlaylistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepo playlistRepo;

    public List<Playlist> getPlaylistsByArtists(List<Artist> artists) {
        List<Playlist> playlists = new ArrayList<>();
        for (Artist artist : artists) {
            playlists.addAll(playlistRepo.findByArtist(artist));
        }
        Collections.shuffle(playlists);
        return playlists;
    }

    public List<Playlist> getRandomPlaylists() {
        List<Playlist> playlists = playlistRepo.findAll();
        Collections.shuffle(playlists);
        return playlists;
    }
}
