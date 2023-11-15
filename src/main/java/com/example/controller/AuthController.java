package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.User;
import com.example.validator.RegistrationValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Controller
@Validated
@RequiredArgsConstructor
public class AuthController {
    @Value("${upload.path}")
    private String uploadPath;
    private final UserImpl userImpl;

    private final RegistrationValidator registrationValidator;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "Registration";
    }

    @PostMapping("/registration/save")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        registrationValidator.validate(user, result, file);

        try {
            String avatar = UUID.randomUUID().toString() + ".jpg";
            file.transferTo(new File(uploadPath + "/" + avatar));
            user.setAvatar(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "Registration";
        }
        userImpl.save(user);
        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегистрировали!");
        return "redirect:/login";
    }

    private static final String[] RANDOM_MESSAGES = {
            "Не забудь только пароль, а то придется создавать новый аккаунт =)",
            "Малоизвестный факт, но в нашем сервисе нету альбомов, потому что я понятия не имею как их реализовывать",
            "Мы скоро доделаем Коллекцию... Наверное... Если будет время... Точнее если все получится)))",
            "Да, у нас нет промокодов на бесплатные подписки, итак трудно жить, какие подписки",
            "Интересное в разделе \"профиль\" никогда не обновляется (",
            "В полной странице артиста нельзя посмотреть все треки артиста, да вот так вот",
            "Хорошо, что многие не видели Musical v1.0 кроме Александра Витальевича, это был полный провал"
    };

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("randomMessage", getRandomMessage());
        return "SignIn";
    }

    @SuppressWarnings("unused")
    private String getRandomMessage() {
        Random random = new Random();
        return RANDOM_MESSAGES[random.nextInt(RANDOM_MESSAGES.length)];
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