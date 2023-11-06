package com.example.controller;

import com.example.impl.TrackImpl;
import com.example.model.Artist;
import com.example.model.Track;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import lombok.RequiredArgsConstructor;
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
public class TrackController {

    @Value("${upload.path}")
    private String uploadPath;
    private final TrackRepo trackRepo;
    private final TrackImpl trackImpl;
    private final ArtistRepo artistRepo;

    @GetMapping("/")
    public String TrackMainPage(Model model) {
        List<Track> tracks = trackRepo.findAll();
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);
        return "AllTracks";
    }

    @PostMapping("alltrack/save")
    public String save(@ModelAttribute("track") Track track, @RequestParam("audio") MultipartFile audio, @RequestParam("cover") MultipartFile cover, @RequestParam("clip") MultipartFile clip, @RequestParam("artist_id") Long artistId) throws IOException {
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
            if (clip != null && !Objects.requireNonNull(clip.getOriginalFilename()).isEmpty()) {
                String filenameClip = UUID.randomUUID() + "." + clip.getOriginalFilename();
                clip.transferTo(new File(uploadPath + "/" + filenameClip));
                track.setClipfilename(filenameClip);
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
                       @RequestParam(value = "clip", required = false) MultipartFile imgfileclip,
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

        if (imgfileclip != null && !imgfileclip.isEmpty()) {
            String filenameclip = UUID.randomUUID() + "." + imgfileclip.getOriginalFilename();
            imgfileclip.transferTo(new File(uploadPath + "/" + filenameclip));
            track.setClipfilename(filenameclip);
        } else {
            Optional<Track> optionalTrack = trackRepo.findById(track.getId());
            if (optionalTrack.isPresent()) {
                Track existingTrack = optionalTrack.get();
                track.setClipfilename(existingTrack.getClipfilename());
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

        trackImpl.editFileds(track.getId(), track.getTitle(), track.getDescription(), track.getLyrics(),
                track.getGenre(), track.getReleaseyear(), track.getAudiofilename(),
                track.getCoverfilename(), track.getClipfilename(), track.getArtists());
        redirectAttributes.addFlashAttribute("success", "Ура качевый трек исправлен бро");
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Track track = trackImpl.findById(id).orElseThrow(() -> new IllegalArgumentException("Инвалид бро" + id));

        String trackAudioPath = track.getAudiofilename();
        String trackCoverPath = track.getCoverfilename();
        String trackClipPath = track.getClipfilename();

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
        if (trackClipPath != null) {
            String imagePath = uploadPath + "/" + trackClipPath;
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
    public String searchArtistAndTrack(@RequestParam(name = "name", required = false) String name, Model model){
        List<Artist> artists = new ArrayList<>();
        List<Track> tracks = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            artists = artistRepo.findByName(name);
            tracks = trackRepo.findByName(name);

            List<Track> artistTracks = new ArrayList<>();
            for (Artist artist: artists) {
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


}
