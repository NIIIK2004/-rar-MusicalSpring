package com.example.controller;


import com.example.model.User;
import com.example.repo.ArtistRepo;
import com.example.repo.TrackRepo;
import com.example.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AdminStatisticsController {
    private final UserRepo userRepo;
    private final ArtistRepo artistRepo;
    private final TrackRepo trackRepo;

    @GetMapping("/Admin")
    //Просмотр страницы статистика для админа
    public String AdminMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminName = authentication.getName();
        User adminUser = userRepo.findByUsername(adminName);
        String adminAvatar = adminUser.getAvatar();
        model.addAttribute("adminAvatar", adminAvatar);
        model.addAttribute("adminName", adminName);

        long totalUsers = userRepo.count();
        long totalArtists = artistRepo.count();
        long totalTracks = trackRepo.count();

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        long newUsersToday = userRepo.countByCreatedDateBetween(today.atStartOfDay(), tomorrow.atStartOfDay());
        long newArtistsToday = artistRepo.countByCreatedDateBetween(today.atStartOfDay(), tomorrow.atStartOfDay());
        long newTracksToday = trackRepo.countByCreatedDateBetween(today.atStartOfDay(), tomorrow.atStartOfDay());

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalArtists", totalArtists);
        model.addAttribute("totalTracks", totalTracks);
        model.addAttribute("newUsersToday", newUsersToday);
        model.addAttribute("newArtistsToday", newArtistsToday);
        model.addAttribute("newTracksToday", newTracksToday);

        model.addAttribute("pageTitle", "Статистика");
        return "/admin/AdminMainPage";
    }

}
