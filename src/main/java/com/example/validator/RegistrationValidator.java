package com.example.validator;

import com.example.impl.UserImpl;
import com.example.model.User;
import lombok.AllArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RegistrationValidator {
    public final UserImpl userImpl;
    private final Tika tika = new Tika();
    public void validate(User user, BindingResult result, MultipartFile file) {

        User existingUser = userImpl.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
            result.rejectValue("username", null, "Пользователь с таким логином уже зарегистрирован!");
        }

        if (user.getUsername().isEmpty()) {
            result.rejectValue("name", null, "Поле 'Логин' не может быть пустым!");
        }

        if (user.getPassword().length() < 6) {
            result.rejectValue("password", null, "Пароль должен содержать не менее 6 символов!");
        }

        if (user.getName().isEmpty()) {
            result.rejectValue("name", null, "Поле 'Имя' не может быть пустым!");
        }

        if (user.getName().matches(".*\\d.*")) {
            result.rejectValue("name", null, "Поле 'Имя' не может содержать в себе цифры");
        }

        if (user.getSurname().isEmpty()) {
            result.rejectValue("surname", null, "Поле 'Фамилия' не может быть пустым!");
        }

        if (user.getSurname().matches(".*\\d.*")) {
            result.rejectValue("name", null, "Поле 'Фамилия' не может содержать в себе цифры");
        }

        if (user.getMail().isEmpty()) {
            result.rejectValue("mail", null, "Поле 'Почта' не может быть пустым!");
        }

//        if (file.isEmpty()) {
//            result.rejectValue("avatar", null, "Выберите пожалуйста любую фотографию!");
//        }

        validateFileType(file, result);

    }

    private void validateFileType(MultipartFile file, BindingResult result) {
        if (file.isEmpty()) {
            result.rejectValue("avatar", null, "Выберите пожалуйста любую фотографию!");
        } else {
            try {
                String mimeType = tika.detect(file.getInputStream());
                if (!mimeType.startsWith("image")) {
                    result.rejectValue("avatar", null, "Поддерживаются только изображения (png, jpg, и т. д.)");
                }
            } catch (IOException e) {
                // Обработка исключения, если что-то пошло не так при проверке типа файла
                e.printStackTrace();
                result.rejectValue("avatar", null, "Ошибка при проверке типа файла");
            }
        }
    }
}
