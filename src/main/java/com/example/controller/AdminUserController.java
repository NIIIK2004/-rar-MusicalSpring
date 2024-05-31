package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.Subscription;
import com.example.model.User;
import com.example.model.UserTrackHistory;
import com.example.repo.SubscriptionRepo;
import com.example.repo.UserTrackHistoryRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserImpl userImpl;
    private final SubscriptionRepo subscriptionRepo;
    private final UserTrackHistoryRepo userTrackHistoryRepo;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    //Просмотр всех пользователей для администратора
    public String viewAllUsers(Model model) {
        List<User> users = userImpl.findAll();
        users = users.stream().sorted(Comparator.comparing(User::getId).reversed()).collect(Collectors.toList());

        model.addAttribute("users", users);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userImpl.findByUsername(currentUsername);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("pageTitle", "Пользователи");

        return "admin/AdminAllUsers";
    }

    @GetMapping("/delete/{id}")
    //Удаление любого пользователя, кроме себя
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = Optional.ofNullable(userImpl.findByUsername(currentUsername));

        if (currentUser.isPresent() && currentUser.get().getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errors", "Вы не можете удалять сами себя");
            return "redirect:/Admin/users";
        }

        User user = userImpl.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + id + " не найден"));
        List<Subscription> userSubscriptions = subscriptionRepo.findByUserId(user.getId());
        List<UserTrackHistory> userTrackHistories = userTrackHistoryRepo.findByUserId(user.getId());

        subscriptionRepo.deleteAll(userSubscriptions);
        userTrackHistoryRepo.deleteAll(userTrackHistories);
        userImpl.delete(id);

        model.addAttribute("success", "Пользователь успешно удален");
        return "redirect:/Admin/users";
    }

    @GetMapping("/add/administrators")
    @PreAuthorize("hasRole('ADMIN')")
    //Просмотр страницы для добавления/создания администратора
    public String addAdministratorsPage(Model model) {
        model.addAttribute("admin", new User());
        model.addAttribute("pageTitle", "Доб.Админ");
        return "admin/CreateAdmin";
    }

    @PostMapping("/add/administrators")
    //Отправка данных для добавления/создания нового админа
    public String addAdministrators(@ModelAttribute("admin") @Valid User admin,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("admin", admin);
            return "admin/CreateAdmin";
        }
        userImpl.saveAdmin(admin);
        redirectAttributes.addFlashAttribute("success", "Новый администратор добавлен успешно");
        return "redirect:/Admin/users";
    }

}
