package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {
    @Value("${upload.path}")
    private String uploadPath;
    private final UserImpl userImpl;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration/save")
    public String registration(@ModelAttribute("user") User user, BindingResult result, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        User existingUser = userImpl.findByUsername(user.getUsername());

        try {
            String avatar = UUID.randomUUID().toString() + ".jpg";
            file.transferTo(new File(uploadPath + "/" + avatar));
            user.setAvatar(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
                        result.rejectValue("username", null,
                    "Пользователь с такие логином уже зарегистрирован!");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "registration";
        }
        userImpl.save(user);
        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегистрировали!");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "SignIn";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            request.getSession().invalidate();
        }
        return "redirect:/login?logout";
    }
}