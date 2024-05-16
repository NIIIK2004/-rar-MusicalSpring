package com.example.controller;

import com.example.dao.SubscriptionDao;
import com.example.impl.ArtistImpl;
import com.example.impl.TrackImpl;
import com.example.impl.UserImpl;
import com.example.model.Artist;
import com.example.model.Subscription;
import com.example.model.Track;
import com.example.model.User;
import com.example.repo.ArtistRepo;
import com.example.repo.SubscriptionRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

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

    @GetMapping(value = {"/allartist", "/Admin-All-Artist"})
    //Получение списка артистов для пользователя и администратора
    public String artistMainPage(Model model, HttpServletRequest request) {
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        model.addAttribute("pageTitle", "Артисты");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "Не авторизован");
        }

        if (request.getRequestURI().equals("/Admin-All-Artist")) {
            return "/admin/AdminAllArtist";
        } else {
            return "AllArtist";
        }
    }

    @GetMapping("/artist/createOrEditArtistPage")
    //Вывод страницы о добавлении либо редактировании артиста
    public String createOrEditArtistPage(Model model, @RequestParam(name = "artistId", required = false) Long artistId) {
        if (artistId != null) {
            Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalThreadStateException("Не найден id Артиста:" + artistId));
            model.addAttribute("artist", artist);
            model.addAttribute("pageTitle", "Ред.Артиста");

        } else {
            model.addAttribute("artist", new Artist());
            model.addAttribute("pageTitle", "Доб.Артиста");

        }
        return "admin/CreateOrEditArtist";
    }

    @PostMapping("/artist/createOrEditArtist")
    //Отправка данных для создания и редактирования артиста
    public String createOrEditArtist(@RequestParam(name = "artistId", required = false) Long artistId,
                                     @RequestParam(name = "file", required = false) MultipartFile file,
                                     @RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam String genre,
                                     @RequestParam String listeners,
                                     @RequestParam String country,
                                     @RequestParam String liking, Model model) throws IOException {
        if (name.isEmpty() || description.isEmpty() || genre.isEmpty() || country.isEmpty()) {
            model.addAttribute("error", "Заполните обязательные поля помеченные звездочкой");
            return "admin/CreateOrEditArtist";
        }

        Artist artist;
        if (artistId != null) {
            artist = artistRepo.findById(artistId)
                    .orElseThrow(() -> new IllegalArgumentException("Артист с ID " + artistId + " не найден"));
        } else {
            artist = new Artist();
            if (artistRepo.existsByName(name)) {
                model.addAttribute("error", "Артист с таким именем уже существует!");
                return "admin/CreateOrEditArtist";
            }
        }

        artist.setName(name);
        artist.setDescription(description);
        artist.setGenre(genre);
        artist.setListeners(listeners);
        artist.setCountry(country);
        artist.setLiking(liking);

        if (file != null && !file.isEmpty()) {
            if (artist.getFilename() != null) {
                File oldFIle = new File(uploadPath + "/" + artist.getFilename());
                if (oldFIle.exists()) {
                    oldFIle.delete();
                }
            }
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            artist.setFilename(filename);
        } else {
            if (artistId != null && artist.getFilename() != null) {
                Artist oldArtist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Артист с ID " + artistId + " не найден"));
                artist.setFilename(oldArtist.getFilename());
            }
        }

        artistImpl.add(artist);
        return "redirect:/Admin-All-Artist";
    }

    @GetMapping("/allartist/delete/{id}")
    //Удаление артиста
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Artist artist = artistImpl.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + id));

        if (artist.getFilename() != null) {
            File oldFIle = new File(uploadPath + "/" + artist.getFilename());
            if (oldFIle.exists()) {
                oldFIle.delete();
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
    //Детальная страница артиста
    public String ArtistDetails(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
        Artist artist = artistRepo.findById(id).orElseThrow(() -> new RuntimeException("Артист с ID : " + id + " не найден!"));
        model.addAttribute("artist", artist);

        List<Artist> recentlyViewedArtists = (List<Artist>) session.getAttribute("recentlyViewedArtists");

        List<Track> lastTracks = artist.getTracks().stream().sorted(Comparator.comparing(Track::getId).reversed()).toList();
        if (lastTracks.size() > 5) {
            lastTracks = lastTracks.subList(0, 6);
        }

        model.addAttribute("lastTracks", lastTracks);

        if (recentlyViewedArtists == null) {
            recentlyViewedArtists = new ArrayList<>();
        }

        if (!recentlyViewedArtists.contains(artist)) {
            recentlyViewedArtists.add(artist);

            if (recentlyViewedArtists.size() >= 6) {
                recentlyViewedArtists = new ArrayList<>(recentlyViewedArtists.subList(recentlyViewedArtists.size() - 6, recentlyViewedArtists.size()));
            }

            session.setAttribute("recentlyViewedArtists", recentlyViewedArtists);
        }

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
        model.addAttribute("playlists", artist.getPlaylists());
        model.addAttribute("albums", artist.getAlbums());

        if (isUserRegistered) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            model.addAttribute("user", user);
        }
        Collections.reverse(recentlyViewedArtists);
        return "Artist";
    }

    @GetMapping("/artist/{id}/similar")
    //Просмотр страницы "Похожие" на артиста
    public String getSimilarArtists(@PathVariable("id") Long id, Model model) {
        Artist artist = artistRepo.findById(id).orElseThrow(() -> new RuntimeException("Артист с ID : " + id + " не найден!"));
        List<Artist> similarArtists = artistRepo.findByGenreAndIdNotOrderByListenersDesc(
                artist.getGenre(),
                artist.getId()
        );
        model.addAttribute("similarArtists", similarArtists);
        model.addAttribute("artist", artist);
        return "ArtistSimilar";
    }
}





