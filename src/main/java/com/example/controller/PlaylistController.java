package com.example.controller;

import com.example.impl.ArtistImpl;
import com.example.model.Artist;
import com.example.model.Playlist;
import com.example.model.PlaylistContent;
import com.example.model.Track;
import com.example.repo.ArtistRepo;
import com.example.repo.PlaylistRepo;
import com.example.repo.TrackRepo;
import com.example.repo.TrackToPlaylistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class PlaylistController {

    @Value("${upload.path}")
    private String uploadPath;
    private final ArtistRepo artistRepo;
    private final TrackRepo trackRepo;
    private final ArtistImpl artistImpl;
    @Autowired
    private final PlaylistRepo playlistRepo;
    private final TrackToPlaylistRepo trackToPlaylistRepo;

    @GetMapping("/artist/{artistId}/playlist/{playlistId}")
    //Это сам плейлист
    public String viewPlaylist(@PathVariable Long artistId, @PathVariable Long playlistId, Model model) {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId));
        Playlist playlist = artist.getPlaylists().stream().filter(a -> a.getId().equals(playlistId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Невалид id плейлиста"));

        List<PlaylistContent> sortedPlaylistByCreated = playlist.getContents().stream().sorted(Comparator.comparing(PlaylistContent::getId).reversed()).toList();

        model.addAttribute("sortedPlaylistByCreated", sortedPlaylistByCreated);
        model.addAttribute("playlist", playlist);
        model.addAttribute("artist", artist);

        return "Playlist";
    }

    @GetMapping("/artist/{artistId}/createOrEditPlaylistPage")
    //Это создание / редактирование плейлиста вид самой страницы
    public String createOrEditPlaylistPage(Model model,
                                           @PathVariable Long artistId,
                                           @RequestParam(name = "playlistId", required = false) Long playlistId) {
        Artist artist = artistRepo.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId));

        Playlist playlist;
        if (playlistId != null) {
            playlist = artist.getPlaylists().stream()
                    .filter(p -> p.getId().equals(playlistId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Не валидный id плейлиста: " + playlistId));
        } else {
            playlist = new Playlist();
        }

        model.addAttribute("playlist", playlist);
        model.addAttribute("pageTitle", "Плейлисты");
        model.addAttribute("artist", artist);
        return "/admin/CreateOrEditPlaylist";
    }

    @PostMapping("artist/{artistId}/createOrEditPlaylist")
    //Это создание / редактирование плейлиста отправка данных, при заполнении полей
    public String createOrEditPlaylist(@PathVariable("artistId") Long artistId,
                                       @ModelAttribute("playlist") Playlist playlist,
                                       @RequestParam(name = "file", required = false) MultipartFile file,
                                       RedirectAttributes redirectAttributes) throws IOException {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId));

        if (playlist.getId() == null || playlist.getId() <= 0) {
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + filename));
                playlist.setCover(filename);
            }
            playlist.setArtist(artist);
            artist.getPlaylists().add(playlist);
        } else {
            Playlist existingPlaylist = artist.getPlaylists().stream().filter(a -> a.getId().equals(playlist.getId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid playlist Id:" + playlist.getId()));
            existingPlaylist.setTitle(playlist.getTitle());
            existingPlaylist.setDescription(playlist.getDescription());

            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + filename));
                playlist.setCover(filename);

                if (existingPlaylist.getCover() != null) {
                    String oldFilePath = uploadPath + "/" + existingPlaylist.getCover();
                    File oldCoverFile = new File(oldFilePath);
                    if (oldCoverFile.exists()) {
                        oldCoverFile.delete();
                    }
                }
                existingPlaylist.setCover(filename);
            }
        }
        artistRepo.save(artist);
        redirectAttributes.addFlashAttribute("saveSuccess", true);
        return "redirect:/artist/{artistId}/details";
    }

    @GetMapping("/artist/{artistId}/playlist/{playlistId}/deletePlaylist")
    //Это удаление плейлиста
    public String deletePlaylist(@PathVariable Long artistId, @PathVariable Long playlistId) {
        Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Невалид артист Id:" + artistId));
        Playlist playlist = artist.getPlaylists().stream().filter(a -> a.getId().equals(playlistId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Невалид плейлист Id:" + playlistId));

        artist.getPlaylists().remove(playlist);
        playlist.setArtist(null);

        String cover = playlist.getCover();
        if (cover != null) {
            String imagePath = uploadPath + "/" + cover;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }

        playlistRepo.deleteById(playlistId);
        return "redirect:/artist/{artistId}/details";
    }

    @GetMapping("/artist/{artistId}/playlist/{playlistId}/addTrack")
    //Это отображение страницы для добавления треков в плейлист
    public String addTracksToPlaylist(Model model, @PathVariable Long artistId, @PathVariable Long playlistId) {
        List<Track> tracks = trackRepo.findAll();

        Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Невалид артист Id:" + artistId));
        Playlist playlist = playlistRepo.findById(playlistId).orElseThrow(() -> new IllegalArgumentException("Невалид плейлист Id:" + playlistId));

        model.addAttribute("playlist", playlist);
        model.addAttribute("tracks", tracks);
        model.addAttribute("artist", artist);
        model.addAttribute("pageTitle", "Плейлист");


        return "/admin/AddTracksToPlaylist";
    }

    @PostMapping("/artist/{artistId}/playlist/{playlistId}/addTracks")
    //Отправка данных на сервер при добавлении треков в плейлист
    public String addSelectedTracksToPlaylist(@PathVariable Long artistId,
                                              @PathVariable Long playlistId,
                                              @RequestParam(value = "selectedTracks", required = false) List<Long> selectedTracksIds,
                                              RedirectAttributes redirectAttributes) {

        Playlist playlist = playlistRepo.findById(playlistId).orElseThrow(() -> new IllegalArgumentException("Невалид плейлист Id:" + playlistId));

        if (selectedTracksIds != null && !selectedTracksIds.isEmpty()) {
            int currentTrackCount = playlist.getContents().size();

            if (currentTrackCount + selectedTracksIds.size() > 15) {
                redirectAttributes.addFlashAttribute("errorMessage", "Невозможно добавить в плейлист более 15 треков.");
                return "redirect:/artist/{artistId}/playlist/{playlistId}";
            }

            List<Track> selectedTracks = trackRepo.findAllById(selectedTracksIds);

            for (Track selectedTrack : selectedTracks) {
                if (playlist.getContents().stream().anyMatch(playlistContent -> playlistContent.getTrack().getId().equals(selectedTrack.getId()))) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Дублирование - Треки не должны повторяться в одном и том же плейлисте");
                    return "redirect:/artist/{artistId}/playlist/{playlistId}";
                }
            }

            for (Track track : selectedTracks) {
                PlaylistContent playlistContent = new PlaylistContent();
                playlistContent.setPlaylist(playlist);
                playlistContent.setTrack(track);

                playlist.getContents().add(playlistContent);
            }

            playlistRepo.save(playlist);
            redirectAttributes.addFlashAttribute("successMessage", "Выбранные треки были добавлены в Плейлист");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ни одного трека не было добавлено");
        }

        return "redirect:/artist/{artistId}/playlist/{playlistId}";
    }

    @GetMapping("/artist/{artistId}/playlist/{playlistId}/deleteTrack")
    //Удаление трека из плейлиста
    public String deleteTrackFromPlaylist(@PathVariable Long artistId,
                                          @PathVariable Long playlistId,
                                          @RequestParam Long trackId,
                                          RedirectAttributes redirectAttributes) {

        Optional<PlaylistContent> playlistContent = trackToPlaylistRepo.findById(trackId);
        if (playlistContent.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "Трек успешно удален из плейлиста");
            PlaylistContent playlistContent1 = playlistContent.get();
            trackToPlaylistRepo.delete(playlistContent1);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Трек не найден в плейлисте");
        }

        return "redirect:/artist/{artistId}/playlist/{playlistId}";
    }



}
