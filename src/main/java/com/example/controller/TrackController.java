package com.example.controller;

import com.example.dto.TrackDTO;
import com.example.impl.TrackImpl;
import com.example.impl.UserImpl;
import com.example.model.*;
import com.example.repo.*;
import com.example.service.PlaylistService;
import com.example.service.TrackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.StringDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserTrackHistoryRepo userTrackHistoryRepo;

    @Autowired
    private PlaylistContentsRepo playlistContentsRepo;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @GetMapping("/{trackId}")
    //Получение значения играющего трека - (создано для передачи fetch-запросам)
    public ResponseEntity<TrackDTO> getTrackById(@PathVariable Long trackId) throws IOException {
        TrackDTO track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }

    @PostMapping("/listen/{trackId}")
    //Отправка запроса на прослушивание определенного трека (Создано для статистики пользователя историй прослушанных треков)
    public ResponseEntity<String> listenToTrack(@PathVariable Long trackId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        Track track = trackRepo.findById(trackId).orElseThrow(() -> new IllegalArgumentException("Трек не найден"));

        List<UserTrackHistory> userTrackHistories = userTrackHistoryRepo.findByUserAndTrack(user, track);
        if (userTrackHistories.isEmpty()) {
            UserTrackHistory newUserTrackHistory = new UserTrackHistory();
            newUserTrackHistory.setUser(user);
            newUserTrackHistory.setTrack(track);
            newUserTrackHistory.setListenCount(1);
            userTrackHistoryRepo.save(newUserTrackHistory);
        } else {
            UserTrackHistory userTrackHistory = userTrackHistories.get(0);
            userTrackHistory.setListenCount(userTrackHistory.getListenCount() + 1);
            userTrackHistoryRepo.save(userTrackHistory);
        }
        return ResponseEntity.ok("Track listened successfully");
    }

    @GetMapping(value = {"/", "/Admin-All-Tracks"})
    //Получение списка всех треков для пользователя и администратора
    public String TrackMainPage(Model model, Principal principal, HttpSession session, HttpServletRequest request) {
        List<Track> tracks = trackRepo.findAll();
        List<Artist> artists = artistRepo.findAll();
        List<Playlist> playlists = new ArrayList<>();

        Collections.shuffle(tracks);

        if (principal != null) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);

            if (user != null) {
                model.addAttribute("name", user.getName());

                List<Subscription> subscriptions = subscriptionRepo.findByUser(user);
                if (subscriptions != null && !subscriptions.isEmpty()) {
                    List<Artist> followedArtists = subscriptions.stream().map(Subscription::getArtist).toList();
                    playlists = playlistService.getPlaylistsByArtists(followedArtists);

                    if (playlists.isEmpty()) {
                        playlists = playlistService.getRandomPlaylists();
                    }
                }
            } else {
                playlists = playlistService.getRandomPlaylists();
            }
        } else {
            playlists = playlistService.getRandomPlaylists();
        }

        model.addAttribute("playlists", playlists);
        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);
        model.addAttribute("volume", 1.0);
        model.addAttribute("pageTitle", "Треки");

        if (principal != null) {
            String username = principal.getName();
            User user = userImpl.findByUsername(username);
            if (user != null) {
                model.addAttribute("name", user.getName());

                boolean showSuccessMessage = session.getAttribute("showSuccessMessage") == null || (boolean) session.getAttribute("showSuccessMessage");
                if (showSuccessMessage) {
                    model.addAttribute("successMessage", "Вы успешно вошли в систему!");
                    session.setAttribute("showSuccessMessage", false);
                }
            }
        }

        List<Artist> recentlyViewedArtists = (List<Artist>) session.getAttribute("recentlyViewedArtists");
        if (recentlyViewedArtists != null && !recentlyViewedArtists.isEmpty()) {
            model.addAttribute("recentlyViewedArtists", recentlyViewedArtists);
        } else {
            Collections.shuffle(artists);
            model.addAttribute("recentlyViewedArtists", artists.subList(0, Math.min(6, artists.size())));
        }
        if (request.getRequestURI().equals("/Admin-All-Tracks")) {
            return "/admin/AdminAllTracks";
        } else {
            return "AllTracks";
        }
    }

    @GetMapping("/track/createOrEditTrackPage")
    //Создание / Редактирование треков - (Получение страницы)
    public String createOrEditTrackPage(Model model, @RequestParam(name = "trackId", required = false) Long trackId) {
        if (trackId != null) {
            Track track = trackRepo.findById(trackId)
                    .orElseThrow(() -> new IllegalArgumentException("Не найден трек с id: " + trackId));
            model.addAttribute("track", track);
            model.addAttribute("pageTitle", "Ред.Трек");

        } else {
            Track track = new Track();
            model.addAttribute("track", track);
            model.addAttribute("pageTitle", "Доб.Трек");
        }
        List<Artist> artists = artistRepo.findAll();
        model.addAttribute("artists", artists);
        return "admin/CreateOrEditTrack";
    }

    @PostMapping("/alltrack/saveOrUpdate")
    //    //Сохранение добавления или редактирования треков - (Отправка запроса)
    public ResponseEntity<?> saveOrUpdateTrack(@ModelAttribute("track") Track track,
                                               @RequestParam(value = "cover", required = false) MultipartFile imgfile,
                                               @RequestParam(value = "audio", required = false) MultipartFile audiofile,
                                               @RequestParam(value = "artist_id", required = false) Long artistId, RedirectAttributes redirectAttributes) throws IOException {
        List<String> errors = new ArrayList<>();

        if (track.getId() == null) {
            if (imgfile == null || imgfile.isEmpty()) {
                errors.add("Загрузите обложку релиза");
            }
            if (audiofile == null || audiofile.isEmpty()) {
                errors.add("Загрузите аудиофайл");
            }
        }

        if (artistId == null) {
            errors.add("Выберите артиста");
        }
        if (track.getTitle().isEmpty()) {
            errors.add("Заполните, название трека");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("errors", errors));
        }

        Artist artist = artistRepo.findById(artistId).orElse(null);
        if (artist == null) {
            errors.add("Выбранный артист не найден");
            return ResponseEntity.badRequest().body(Collections.singletonMap("errors", errors));
        }

        track.setArtists(artist);

        Track existingTrack = track.getId() != null ? trackRepo.findById(track.getId()).orElse(null) : null;

        if (imgfile != null && !imgfile.isEmpty()) {
            String filenameimg = UUID.randomUUID() + "." + imgfile.getOriginalFilename();
            imgfile.transferTo(new File(uploadPath + "/" + filenameimg));
            track.setCoverfilename(filenameimg);

            if (existingTrack != null && existingTrack.getCoverfilename() != null) {
                File oldImgFile = new File(uploadPath + "/" + existingTrack.getCoverfilename());
                oldImgFile.delete();
            }
        } else {
            if (existingTrack != null && existingTrack.getCoverfilename() != null) {
                track.setCoverfilename(existingTrack.getCoverfilename());
            }
        }

        if (audiofile != null && !audiofile.isEmpty()) {
            String filenameaudio = UUID.randomUUID() + "." + audiofile.getOriginalFilename();
            audiofile.transferTo(new File(uploadPath + "/" + filenameaudio));
            track.setAudiofilename(filenameaudio);

            if (existingTrack != null && existingTrack.getAudiofilename() != null) {
                File oldAudioFile = new File(uploadPath + "/" + existingTrack.getAudiofilename());
                oldAudioFile.delete();
            }
        } else {
            if (existingTrack != null && existingTrack.getAudiofilename() != null) {
                track.setAudiofilename(existingTrack.getAudiofilename());
            }
        }

        trackRepo.save(track);

        redirectAttributes.addFlashAttribute("successAdd", "Релиз был успешно загружен");
        return ResponseEntity.ok(Collections.singletonMap("redirectUrl", "/Admin-All-Tracks"));
    }


    @GetMapping("/delete/{id}")
    //Удаление трека и удаление всей логики связанных с другими таблицами (Получение запроса - Удаление)
    public String Delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Track track = trackImpl.findById(id).orElseThrow(() -> new IllegalArgumentException("Инвалид бро" + id));
        List<UserTrackHistory> userTrackHistoryList = userTrackHistoryRepo.findByTrackId(id);
        List<PlaylistContent> playlistContent = playlistContentsRepo.findByTrackId(id);

        if (!userTrackHistoryList.isEmpty()) {
            userTrackHistoryRepo.deleteByTrack(track);
        }
        if (!playlistContent.isEmpty()) {
            playlistContentsRepo.deleteByTrack(track);
        }

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

        trackImpl.delete(track.getId());
        redirectAttributes.addFlashAttribute("successDelete", "Релиз был успешно удален");

        return "redirect:/Admin-All-Tracks";
    }

    @GetMapping("/search")
    //Поиск треков и артистов
    public String searchArtistAndTrack(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Artist> artists;
        List<Track> tracks;
        List<String> suggestions = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            artists = artistRepo.findByName(name);
            tracks = trackRepo.findByName(name);

            if (artists.isEmpty() && tracks.isEmpty()) {
                suggestions = generateSuggestions(name);
            } else {
                List<Track> artistTracks = new ArrayList<>();
                for (Artist artist : artists) {
                    artistTracks.addAll(artist.getTracks());
                }
                tracks.addAll(artistTracks);
            }
        } else {
            artists = artistRepo.findAll().stream().limit(3).collect(Collectors.toList());
            tracks = trackRepo.findAll().stream().limit(5).collect(Collectors.toList());
        }

        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);
        model.addAttribute("suggestions", suggestions);
        model.addAttribute("query", name);

        return "Search";
    }

    private List<String> generateSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        StringDistance distance = new LevensteinDistance();

        List<String> allNames = new ArrayList<>();
        artistRepo.findAll().forEach(artist -> allNames.add(artist.getName()));
        trackRepo.findAll().forEach(track -> allNames.add(track.getTitle()));

        for (String name : allNames) {
            if (distance.getDistance(query, name) > 0.8) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

    @GetMapping("/random-track")
    //Получение запроса для реализации включения рандомных треков
    public ResponseEntity<TrackDTO> getRandomTrack() {
        TrackDTO randomTrack = trackService.getRandomTrack();
        return ResponseEntity.ok(randomTrack);
    }


}
