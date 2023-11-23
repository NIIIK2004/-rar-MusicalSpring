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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/artist/{artistId}/createOrEditAlbumPage") //Видим редактирование Альбома
    public String createOrEditAlbumPage(Model model, @PathVariable Long artistId, @RequestParam(name = "albumId", required = false) Long albumId) {
        Artist artist = artistRepo.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId));

        if (albumId != null) {
            Album album = artist.getAlbums().stream()
                    .filter(a -> a.getId().equals(albumId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + albumId));
            model.addAttribute("album", album);
        } else {
            model.addAttribute("album", new Album());
        }
        model.addAttribute("artist", artist);
        return "admin/CreateOrEditPhotoAlbum";
    }

    @PostMapping("artist/{artistId}/createOrEditAlbum") //Редактируем или создаём новый Альбом
    public String createOrEditAlbum(@PathVariable("artistId") Long artistId,
                                    @ModelAttribute("album") Album album,
                                    @RequestParam(name = "file", required = false)
                                    MultipartFile file,
                                    RedirectAttributes redirectAttributes) throws IOException {
        Artist artist = artistImpl.findById(artistId).orElseThrow(() -> new IllegalArgumentException("Не найден id Артиста:" + artistId));

        if (album.getId() == null || album.getId() <= 0) {
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + filename));
                album.setFilename(filename);
            }
            album.setArtist(artist);
            artist.getAlbums().add(album);
        } else {
            Album existingAlbum = artist.getAlbums().stream().filter(a -> a.getId().equals(album.getId())).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid album Id:" + album.getId()));

            existingAlbum.setName(album.getName());
            existingAlbum.setCity(album.getCity());
            existingAlbum.setYear(album.getYear());

            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                String filename = UUID.randomUUID() + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + filename));
                album.setFilename(filename);

                if (existingAlbum.getFilename() != null) {
                    String oldFilePath = uploadPath + "/" + existingAlbum.getFilename();
                    File oldCoverFile = new File(oldFilePath);
                    if (oldCoverFile.exists()) {
                        oldCoverFile.delete();
                    }
                }
                existingAlbum.setFilename(filename);
            }
        }
        artistRepo.save(artist);
        redirectAttributes.addFlashAttribute("saveSuccess", true);
        return "redirect:/artist/{artistId}/details#ArtistAlbums";
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
