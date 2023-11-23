package com.example.controller;

import com.example.dao.SubscriptionDao;
import com.example.impl.ArtistImpl;
import com.example.impl.TrackImpl;
import com.example.impl.UserImpl;
import com.example.model.*;
import com.example.repo.ArtistRepo;
import com.example.repo.SubscriptionRepo;
import com.example.repo.TrackRepo;
import jakarta.servlet.http.HttpServletRequest;
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
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AllArtistController {

    @Autowired
    private UserImpl userImpl;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private SubscriptionDao subscriptionDao;
    @Value("${upload.path}")
    private String uploadPath;
    private final ArtistRepo artistRepo;
    private final ArtistImpl artistImpl;

    private final TrackImpl trackImpl;
    private final TrackRepo trackRepo;
    private Track track;

    @GetMapping(value = {"/allartist", "/Admin-All-Artist"})
    public String artistMainPage(Model model, HttpServletRequest request) {
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);

        if (request.getRequestURI().equals("/Admin-All-Artist")) {
            return "/admin/AdminAllArtist";
        } else {
            return "allartist";
        }
    }

    @GetMapping("/artist/createOrEditArtistPage")
    public String createOrEditArtistPage(Model model, @RequestParam(name = "artistId", required = false) Long artistId) {

        if (artistId != null) {
            Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalThreadStateException("Не найден id Артиста:" + artistId));
            model.addAttribute("artist", artist);
        } else {
            model.addAttribute("artist", new Artist());
        }
        return "admin/CreateOrEditArtist";
    }

    @PostMapping("/artist/createOrEditArtist")
    public String createOrEditArtist(@RequestParam(name = "artistId", required = false) Long artistId,
                                     @RequestParam(name = "file", required = false) MultipartFile file,
                                     @RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam String genre,
                                     @RequestParam String listeners,
                                     @RequestParam String country,
                                     @RequestParam String liking) throws IOException {

        Artist artist = (artistId != null) ? artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId)) :
                new Artist();

        artist.setName(name);
        artist.setDescription(description);
        artist.setGenre(genre);
        artist.setListeners(listeners);
        artist.setCountry(country);
        artist.setLiking(liking);


        if (file != null && !file.isEmpty()) {
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            artist.setFilename(filename);

            System.out.println("File saved successfully: " + filename);
            System.out.println("File path: " + uploadPath + "/" + filename);
            System.out.println("Artist: " + artist);
            System.out.println("Artist Filename: " + artist.getFilename());

            if (artistId != null) {
                Optional<Artist> optionalArtist = artistRepo.findById(artistId);
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
            }
        } else if (artistId != null) {
            Optional<Artist> optionalArtist = artistRepo.findById(artistId);
            if (optionalArtist.isPresent()) {
                Artist existingArtist = optionalArtist.get();
                artist.setFilename(existingArtist.getFilename());
            }
        }
        artistImpl.add(artist);
        return "redirect:/Admin-All-Artist";
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


        List<Track> tracks = artist.getTracks();
        if (tracks != null) {
            for (Track track1 : tracks) {
                trackImpl.delete(track1.getId());
            }
        }

        List<Subscription> subscriptions = subscriptionRepo.findByArtistId(artist.getId());
        subscriptionRepo.deleteAll(subscriptions);

        artistImpl.delete(artist.getId());
        redirectAttributes.addFlashAttribute("success", "Успешное удаление артиста " + artist.getName());

        return "redirect:/Admin-All-Artist";
    }

    @GetMapping("/artist/{id}/details")
    public String ArtistDetails(@PathVariable("id") Long id, Model model, Principal principal) {
        Artist artist = artistRepo.findById(id).orElseThrow(() -> new RuntimeException("Артист с ID : " + id + " не найден!"));
        model.addAttribute("artist", artist);

        Track latestTrack = trackImpl.findLatestTrackByArtistId(artist.getId());
        if (latestTrack == null) {
            model.addAttribute("noTracks", true);
        } else {
            model.addAttribute("latestTrack", latestTrack);
        }


        boolean isSubscribed = false;
        boolean isUserRegistered = principal != null;

        if (isUserRegistered) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            isSubscribed = subscriptionDao.checkSubscription(user.getId(), artist.getId());
        }

        model.addAttribute("isSubscribed", isSubscribed);
        model.addAttribute("isUserRegistered", isUserRegistered);
        model.addAttribute("albums", artist.getAlbums());

        if (isUserRegistered) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            model.addAttribute("user", user);
        }

        return "Artist";
    }
}



