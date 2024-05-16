package com.example.validator;

import com.example.impl.UserImpl;
import com.example.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    public final UserImpl userImpl;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        //Проверка логина
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.rejectValue("username", "NotEmpty.user.username", "Поле 'Логин' не может быть пустым!");
        } else if (user.getUsername().length() > 30) {
            errors.rejectValue("username", "Size.user.username", "Поле 'Логин' не может превышать более 30 символов!");
        } else if (usernameAlreadyExists(user)) {
            errors.rejectValue("username", "Duplicate.user.username", "Пользователь с таким логином уже зарегистрирован!");
        }

        //Проверка пароля
//        if (user.getPassword() != null && user.getPassword().length() < 6) {
//            errors.rejectValue("password", "Size.user.password", "Пароль должен содержать не менее 6 символов!");
//        }

        // Проверка имени
        if (user.getName() == null || user.getName().isEmpty()) {
            errors.rejectValue("name", "NotEmpty.user.name", "Поле 'Имя' не может быть пустым!");
        } else if (user.getName().length() > 30) {
            errors.rejectValue("name", "Size.user.name", "Поле 'Имя' не может превышать 30 символов!");
        } else if (!user.getName().matches("[а-яА-Яa-zA-Z]+")) {
            errors.rejectValue("name", "Pattern.user.name", "Поле 'Имя' не может содержать цифры!");
        }

        // Проверка фамилии
        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            errors.rejectValue("surname", "NotEmpty.user.surname", "Поле 'Фамилия' не может быть пустым!");
        } else if (user.getSurname().length() > 30) {
            errors.rejectValue("surname", "Size.user.surname", "Поле 'Фамилия' не может превышать 30 символов!");
        } else if (!user.getSurname().matches("[а-яА-Яa-zA-Z]+")) {
            errors.rejectValue("surname", "Pattern.user.surname", "Поле 'Фамилия' не может содержать цифры!");
        }

        // Проверка почты
        if (user.getMail() == null || user.getMail().isEmpty()) {
            errors.rejectValue("mail", "NotEmpty.user.mail", "Поле 'Почта' не может быть пустым!");
        }

    }

    private boolean usernameAlreadyExists(User user) {
        User existingUser = userImpl.findByUsername(user.getUsername());
        return existingUser != null && !existingUser.getId().equals(user.getId());
    }

}
