package com.example.controller;

import com.example.impl.UserImpl;
import com.example.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserImpl userImpl;

    @GetMapping
    public String viewAllUsers(Model model) {
        List<User> users = userImpl.findAll();
        users = users.stream().sorted(Comparator.comparing(User::getId).reversed()).collect(Collectors.toList());

        model.addAttribute("users", users);
        return "admin/AdminAllUsers";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
       userImpl.delete(id);
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
