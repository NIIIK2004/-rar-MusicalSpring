package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.User;
import com.example.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserImpl userImpl;
    private final UserRepo userRepo;

    @GetMapping("/settings")
    public String profilePage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userImpl.findByUsername(username);
        if (user == null) {
            return "redirect:/login?logout";
        }
        model.addAttribute("user", user);
        return "setting";
    }

    @PostMapping("/settings/save")
    public String edit(@ModelAttribute("user") User user,
                       RedirectAttributes redirectAttributes) {

        Optional<User> optionalUser = userRepo.findById(user.getId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
        }
        userImpl.updateFields(user.getId(), user.getName(), user.getSurname(), user.getMail());
        redirectAttributes.addFlashAttribute("success", "Успешное редактирование! Пользователь - " + user.getUsername());
        return "redirect:/settings";
    }
}