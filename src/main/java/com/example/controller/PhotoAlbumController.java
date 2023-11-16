package com.example.controller;

import com.example.impl.ArtistImpl;
import com.example.model.Album;
import com.example.model.Artist;
import com.example.model.PhotoArtists;
import com.example.repo.AlbumRepo;
import com.example.repo.ArtistRepo;
import com.example.repo.PhotoToAlbumRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PhotoAlbumController {
    @Value("${upload.path}")
    private String uploadPath;
    private final ArtistRepo artistRepo;
    private final ArtistImpl artistImpl;
    @Autowired
    private final PhotoToAlbumRepo photoToAlbumRepo;
    @Autowired
    private final AlbumRepo albumRepo;


    @GetMapping("/artist/{artistId}/album/{albumId}") //Видим сам альбом внутри
    public String viewAlbum(@PathVariable Long artistId, @PathVariable Long albumId, Model model) {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + artistId));

        Album album = artist.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Невалид id альбома"));

        List<PhotoArtists> sortedPhotosByCreatedDateTime = album.getPhotos().stream().sorted(Comparator.comparing(PhotoArtists::getId).reversed()).collect(Collectors.toList());

        model.addAttribute("album", album);
        model.addAttribute("artist", artist);
        model.addAttribute("sortedPhotoDate", sortedPhotosByCreatedDateTime);

        return "PhotoAlbum";
    }

    @PostMapping("/artist/{id}/createAlbum") //Создаем Альбом
    public String createAlbum(@PathVariable("id") Long id, @ModelAttribute("album") Album album, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            album.setFilename(filename);
        }
        Artist artist = artistImpl.findById(id).orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + id));
        album.setArtist(artist);
        artist.getAlbums().add(album);
        artistRepo.save(artist);
        return "redirect:/artist/{id}/details";
    }

    @GetMapping("/artist/{artistId}/album/{albumId}/editPhotoAlbumPage") //Видим редактирование Альбома
    public String editPhotoAlbumPage(Model model, @PathVariable Long artistId, @PathVariable Long albumId) {
        Optional<Artist> artistOptional = artistRepo.findById(artistId);
        Optional<Album> albumOptional = albumRepo.findById(albumId);
        if (artistOptional.isPresent() && albumOptional.isPresent()) {
            Artist artist = artistOptional.get();
            Album album = albumOptional.get();

            model.addAttribute("artist", artist);
            model.addAttribute("album", album);
            return "admin/EditPhotoAlbum";
        } else {
            return "error";
        }
    }

    @PostMapping("/artist/{artistId}/album/{albumId}/editPhotoAlbum") //Редактируем Альбом
    public String editPhotoAlbum(Model model, @PathVariable Long artistId, @PathVariable Long albumId,
                                 @RequestParam("name") String name,
                                 @RequestParam("city") String city,
                                 @RequestParam("year") int year,
                                 @RequestParam("file") MultipartFile file) throws IOException {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + artistId));

        Album album = artist.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + albumId));

        album.setName(name);
        album.setCity(city);
        album.setYear(year);

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));

            if (album.getFilename() != null) {
                String oldCoverPath = uploadPath + "/" + album.getFilename();
                File oldCoverFile = new File(oldCoverPath);
                if (oldCoverFile.exists()) {
                    oldCoverFile.delete();
                }
            }

            model.addAttribute("artist", artist);
            model.addAttribute("album", album);

            album.setFilename(filename);
        }

        artistRepo.save(artist);

        return "redirect:/artist/" + artistId + "/details";
    }

    @GetMapping("/artist/{artistId}/album/{albumId}/deleteAlbum") //Удаляем Альбом
    public String deleteAlbum(@PathVariable Long artistId, @PathVariable Long albumId) {
        Artist artist = artistRepo.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + artistId));
        Album album = artist.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + albumId));

        artist.getAlbums().remove(album);
        album.setArtist(null);

        albumRepo.deleteById(albumId);
        return "redirect:/artist/{artistId}/details";
    }

    @PostMapping("/artist/{artistId}/album/{albumId}/addPhoto") //Добавляем фото в Альбом
    public String addPhotoToAlbum(@PathVariable Long artistId, @PathVariable Long albumId, @RequestParam("files") MultipartFile[] files) throws IOException {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Invalid artist Id:" + artistId));
        Album album = artist.getAlbums().stream().filter(a -> a.getId().equals(albumId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + albumId));

        for (MultipartFile file : files) {
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + filename));

                PhotoArtists photoArtists = new PhotoArtists();
                photoArtists.setFilename(filename);
                photoArtists.setAlbum(album);

                album.getPhotos().add(photoArtists);
            }
        }
        artistRepo.save(artist);

        return "redirect:/artist/{artistId}/album/{albumId}";
    }

    @GetMapping("/artist/{artistId}/album/{albumId}/deletePhoto/{photoId}") //Удаляем фото из Альбома
    public String deletePhotoToAlbum(@PathVariable Long artistId, @PathVariable Long albumId, @PathVariable Long photoId) {
        Optional<PhotoArtists> optionalPhotoArtists = photoToAlbumRepo.findById(photoId);
        if (optionalPhotoArtists.isPresent()) {
            PhotoArtists photoArtists = optionalPhotoArtists.get();
            photoToAlbumRepo.delete(photoArtists);
        }

        return "redirect:/artist/{artistId}/album/{albumId}";
    }



}
