package com.example.controller;

import com.example.dto.TrackDTO;
import com.example.impl.TrackImpl;
import com.example.impl.UserImpl;
import com.example.model.Artist;
import com.example.model.Track;
import com.example.model.User;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import com.example.service.TrackService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class TrackController {

    @Value("${upload.path}")
    private String uploadPath;
    private final TrackRepo trackRepo;
    private final TrackImpl trackImpl;
    private final ArtistRepo artistRepo;

    private final UserImpl userImpl;

    @Autowired
    private TrackService trackService;

    @GetMapping("/{trackId}")
    public ResponseEntity<TrackDTO> getTrackById(@PathVariable Long trackId) throws IOException {
        TrackDTO track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }

    @GetMapping("/")
    public String TrackMainPage(Model model, Principal principal, HttpSession session) {
        List<Track> tracks = trackRepo.findAll();
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);
        model.addAttribute("volume", 1.0);
        if (principal != null) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            if (user != null) {
                model.addAttribute("name", user.getName());
            }
        }

        List<Artist> recentlyViewedArtists = (List<Artist>) session.getAttribute("recentlyViewedArtists");
        if (recentlyViewedArtists != null && !recentlyViewedArtists.isEmpty()) {
            model.addAttribute("recentlyViewedArtists", recentlyViewedArtists);
        } else {
            Collections.shuffle(artists);
            model.addAttribute("recentlyViewedArtists", artists.subList(0, Math.min(6, artists.size())));
        }

        return "AllTracks";
    }

    @GetMapping("/addTrack")
    public String CreateOrEditTrack(@ModelAttribute("track") Track track, Model model) throws IOException {
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        return "admin/CreateOrEditTrack";
    }

    @PostMapping("alltrack/save")
    public String save(@ModelAttribute("track") Track track,
                       @RequestParam("audio") MultipartFile audio,
                       @RequestParam("cover") MultipartFile cover,
                       @RequestParam("artist_id") Long artistId) throws IOException {
        Artist artist = artistRepo.findById(artistId).orElse(null);
        if (artist != null) {
            track.setArtists(artist);
            if (audio != null && !Objects.requireNonNull(audio.getOriginalFilename()).isEmpty()) {
                String filenameAudio = UUID.randomUUID() + "." + audio.getOriginalFilename();
                audio.transferTo(new File(uploadPath + "/" + filenameAudio));
                track.setAudiofilename(filenameAudio);
            }
            if (cover != null && !Objects.requireNonNull(cover.getOriginalFilename()).isEmpty()) {
                String filenameCover = UUID.randomUUID() + "." + cover.getOriginalFilename();
                cover.transferTo(new File(uploadPath + "/" + filenameCover));
                track.setCoverfilename(filenameCover);
            }
            track.setArtists(artist);
            trackRepo.save(track);
            trackImpl.add(track);
            System.out.println("Запрос каким то образом отправлен магия не более!");
        }
        System.out.println("artistId: " + artistId);
        return "redirect:/";
    }

    @PostMapping("alltrack/edit")
    public String edit(@ModelAttribute("track") Track track,
                       @RequestParam(value = "cover", required = false) MultipartFile imgfile,
                       @RequestParam(value = "audio", required = false) MultipartFile audiofile,
                       @RequestParam("artist_id") Long artistId,
                       RedirectAttributes redirectAttributes) throws IOException {

        if (imgfile != null && !imgfile.isEmpty()) {
            String filenameimg = UUID.randomUUID() + "." + imgfile.getOriginalFilename();
            imgfile.transferTo(new File(uploadPath + "/" + filenameimg));
            track.setCoverfilename(filenameimg);
        } else {
            Optional<Track> optionalTrack = trackRepo.findById(track.getId());
            if (optionalTrack.isPresent()) {
                Track existingTrack = optionalTrack.get();
                track.setCoverfilename(existingTrack.getCoverfilename());
            }
        }


        if (audiofile != null && !audiofile.isEmpty()) {
            String filenameaudio = UUID.randomUUID() + "." + audiofile.getOriginalFilename();
            audiofile.transferTo(new File(uploadPath + "/" + filenameaudio));
            track.setAudiofilename(filenameaudio);
        } else {
            Optional<Track> optionalTrack = trackRepo.findById(track.getId());
            if (optionalTrack.isPresent()) {
                Track existingTrack = optionalTrack.get();
                track.setAudiofilename(existingTrack.getAudiofilename());
            }
        }
        Artist artist = artistRepo.findById(artistId).orElse(null);
        track.setArtists(artist);

        trackImpl.editFileds(track.getId(), track.getTitle(), track.getLyrics(),
                track.getGenre(), track.getReleaseyear(), track.getAudiofilename(),
                track.getCoverfilename(), track.getArtists());
        redirectAttributes.addFlashAttribute("success", "Ура качевый трек исправлен бро");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Track track = trackImpl.findById(id).orElseThrow(() -> new IllegalArgumentException("Инвалид бро" + id));

        String trackAudioPath = track.getAudiofilename();
        String trackCoverPath = track.getCoverfilename();

        if (trackAudioPath != null) {
            String imagePath = uploadPath + "/" + trackAudioPath;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
        if (trackCoverPath != null) {
            String imagePath = uploadPath + "/" + trackCoverPath;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
        trackImpl.delete(track.getArtists().getId());
        trackImpl.delete(track.getId());
        redirectAttributes.addFlashAttribute("success", "Эх мы удалили какой то хороший трек грусть..." + track.getTitle());

        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchArtistAndTrack(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Artist> artists = new ArrayList<>();
        List<Track> tracks = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            artists = artistRepo.findByName(name);
            tracks = trackRepo.findByName(name);

            List<Track> artistTracks = new ArrayList<>();
            for (Artist artist : artists) {
                artistTracks.addAll(artist.getTracks());
            }
            tracks.addAll(artistTracks);
        } else {
            artists = artistRepo.findAll();
            tracks = trackRepo.findAll();
        }
        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);

        return "Search";
    }

    @GetMapping("/random-track")
    public ResponseEntity<TrackDTO> getRandomTrack() {
        TrackDTO randomTrack = trackService.getRandomTrack();
        return ResponseEntity.ok(randomTrack);
    }


}
