package com.example.controller;

import com.example.impl.ArtistImpl;
import com.example.impl.TrackImpl;
import com.example.model.Artist;
import com.example.model.Track;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AllArtistController {

    @Value("${upload.path}")
    private String uploadPath;
    private final ArtistRepo artistRepo;
    private final ArtistImpl artistImpl;

    private final TrackImpl trackImpl;
    private final TrackRepo trackRepo;


    private Track track;

    @GetMapping("/allartist")
    public String artistMainPage(Model model) {
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        return "allartist";
    }

    @PostMapping("allartist/save")
    public String save(@ModelAttribute("artist") Artist artist, @RequestParam("photo") MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            artist.setFilename(filename);
        }
        artistImpl.add(artist);
        System.out.print("Запрос отправлен!");
        return "redirect:/allartist";
    }

    @PostMapping("allartist/edit")
    public String edit(@ModelAttribute("artist") Artist artist,
                       @RequestParam(value = "photo", required = false) MultipartFile file,
                       RedirectAttributes redirectAttributes) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            artist.setFilename(filename);

            Optional<Artist> optionalArtist = artistRepo.findById(artist.getId());
            if (optionalArtist.isPresent()) {
                Artist existingArtist = optionalArtist.get();
                String oldFile = existingArtist.getFilename();
                if (oldFile != null) {
                    String oldFilePath = uploadPath + "/" + oldFile;
                    File oldFilename = new File(oldFilePath);
                    if (oldFilename.exists()) {
                        oldFilename.delete();
                    }
                }
            }
        } else {
            Optional<Artist> optionalArtist = artistRepo.findById(artist.getId());
            if (optionalArtist.isPresent()) {
                Artist existingArtist = optionalArtist.get();
                artist.setFilename(existingArtist.getFilename());
            }
        }

        artistImpl.editFields(artist.getId(), artist.getName(), artist.getDescription(),
                artist.getGenre(), artist.getListeners(), artist.getFilename());

        redirectAttributes.addFlashAttribute("success", "Успешное редактирование! Артист - " + artist.getName());
        return "redirect:/allartist";
    }

    @GetMapping("/allartist/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Artist artist = artistImpl.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + id));

        String artistImagePath = artist.getFilename();

        if (artistImagePath != null) {
            String imagePath = uploadPath + "/" + artistImagePath;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
        artistImpl.delete(artist.getId());

        List<Track> tracks = artist.getTracks();
        if (tracks != null) {
            for (Track track1 : tracks) {
                trackImpl.delete(track1.getId());
            }
        }


        redirectAttributes.addFlashAttribute("success", "Успешное удаление артиста " + artist.getName());

        return "redirect:/allartist";
    }

    @GetMapping("/artist/{id}/details")
    public String ArtistDetails(@PathVariable("id") Long id, Model model) {
        Artist artist = artistRepo.findById(id).orElseThrow(() -> new RuntimeException("Артист с ID : " + id + " не найден!"));
        model.addAttribute("artist", artist);

        Track latestTrack = trackImpl.findLatestTrackByArtistId(artist.getId());
        if (latestTrack == null) {
            model.addAttribute("noTracks", true);
        } else {
            model.addAttribute("latestTrack", latestTrack);
        }

        return "Artist";
    }


}
