package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.Subscription;
import com.example.model.User;
import com.example.repo.SubscriptionRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String viewAllUsers(Model model) {
        List<User> users = userImpl.findAll();
        users = users.stream().sorted(Comparator.comparing(User::getId).reversed()).collect(Collectors.toList());

        model.addAttribute("users", users);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userImpl.findByUsername(currentUsername);
        model.addAttribute("currentUserId", currentUser.getId()); // Передаем только идентификатор текущего пользователя

        return "admin/AdminAllUsers";
    }

    @GetMapping("/delete/{id}")
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

        subscriptionRepo.deleteAll(userSubscriptions);
        userImpl.delete(id);

        model.addAttribute("success", "Пользователь успешно удален");
        return "redirect:/Admin/users";
    }

    @GetMapping("/add/administrators")
    public String addAdministratorsPage(Model model) {
        model.addAttribute("admin", new User());
        return "admin/CreateAdmin";
    }

    @PostMapping("/add/administrators")
    public String addAdministrators(@ModelAttribute("admin") @Valid User admin,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", "Произошли ошибки попробуйте чуть позже");
            return "redirect:/Admin/users";
        }
        userImpl.saveAdmin(admin);
        redirectAttributes.addFlashAttribute("success", "Новый администратор добавлен успешно");
        return "redirect:/Admin/users";
    }

}
