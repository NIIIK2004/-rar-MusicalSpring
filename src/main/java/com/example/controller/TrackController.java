package com.example.controller;

import com.example.dto.TrackDTO;
import com.example.impl.TrackImpl;
import com.example.impl.UserImpl;
import com.example.model.Artist;
import com.example.model.Track;
import com.example.model.User;
import com.example.model.UserTrackHistory;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import com.example.repo.UserRepo;
import com.example.repo.UserTrackHistoryRepo;
import com.example.service.TrackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
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

//        tracks = tracks.stream().sorted(Comparator.comparing(Track::getId).reversed()).toList();
        Collections.shuffle(tracks);

        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);
        model.addAttribute("volume", 1.0);
        model.addAttribute("pageTitle", "Треки");

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
    //Сохранение добавления или редактирования треков - (Отправка запроса)
    public String saveOrUpdateTrack(@ModelAttribute("track") Track track,
                                    @RequestParam(value = "cover", required = false) MultipartFile imgfile,
                                    @RequestParam(value = "audio", required = false) MultipartFile audiofile,
                                    @RequestParam(value = "artist_id", required = false) Long artistId,
                                    RedirectAttributes redirectAttributes) throws IOException {
        if (artistId == null) {
            redirectAttributes.addFlashAttribute("error", "Пожалуйста, выберите артиста");
            return "redirect:/track/createOrEditTrackPage";
        }

        Artist artist = artistRepo.findById(artistId).orElse(null);

        if (artist != null) {
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

            redirectAttributes.addFlashAttribute("success", track.getId() == null ? "Новый трек успешно добавлен!" : "Трек успешно обновлен!");
            return "redirect:/Admin-All-Tracks";
        } else {
            redirectAttributes.addFlashAttribute("error", "Выбранный артист не найден");
            return "redirect:/track/createOrEditTrackPage";
        }
    }


    @GetMapping("/delete/{id}")
    //Удаление трека и удаление всей логики связанных с другими таблицами (Получение запроса - Удаление)
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
        userTrackHistoryRepo.deleteByTrack(track);

        trackImpl.delete(track.getId());
        redirectAttributes.addFlashAttribute("success", "Эх мы удалили какой то хороший трек грусть..." + track.getTitle());

        return "redirect:/Admin-All-Tracks";
    }

    @GetMapping("/search")
    //Поиск треков и артистов
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
            artists = artistRepo.findAll().stream().limit(3).collect(Collectors.toList());
            tracks = trackRepo.findAll().stream().limit(5).collect(Collectors.toList());
        }
        model.addAttribute("artists", artists);
        model.addAttribute("tracks", tracks);

        return "Search";
    }



    @GetMapping("/random-track")
    //Получение запроса для реализации включения рандомных треков
    public ResponseEntity<TrackDTO> getRandomTrack() {
        TrackDTO randomTrack = trackService.getRandomTrack();
        return ResponseEntity.ok(randomTrack);
    }


}
