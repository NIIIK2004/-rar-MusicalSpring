package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.Subscription;
import com.example.model.User;
import com.example.repo.SubscriptionRepo;
import com.example.repo.UserRepo;
import com.example.validator.RegistrationValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    @Value("${upload.path}")
    private String uploadPath;
    private final UserImpl userImpl;
    private final UserRepo userRepo;
    private final SubscriptionRepo subscriptionRepo;

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login?logout";
        }
        String username = principal.getName();
        User user = userImpl.findByUsername(username);
        if (user == null) {
            return "redirect:/login?logout";
        }
        List<Subscription> subscription = subscriptionRepo.getAllSubscriptionsByUser(user);
        model.addAttribute("subscription", subscription);
        model.addAttribute("user", user);
        return "Profile";
    }

    @GetMapping("/setting")
    public String settingUser(Model model, Principal principal) {
        String username = principal.getName();
        User user = userImpl.findByUsername(username);
        if (user == null) {
            return "redirect:/login?logout";
        }
        model.addAttribute("user", user);
        return "Setting";
    }

    @PostMapping("/setting/save")
    public String settingUserSave(@ModelAttribute("user") User user, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        Optional<User> optionalUser = userRepo.findById(user.getId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (file != null) {
                if (file.isEmpty()) {
                    user.setAvatar(existingUser.getAvatar());
                } else {
                    try {
                        String avatar = UUID.randomUUID().toString() + ".jpg";
                        file.transferTo(new File(uploadPath + "/" + avatar));
                        user.setAvatar(avatar);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        userImpl.updateFields(user.getId(), user.getName(), user.getSurname(), user.getMail(), user.getUsername(), user.getPassword(), user.getAvatar());
        redirectAttributes.addFlashAttribute("message", "Данные пользователя изменены");
        return "redirect:/profile";
    }

}