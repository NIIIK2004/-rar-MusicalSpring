package com.example.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        }
        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        }
        if (status != null && Integer.parseInt(status.toString()) == HttpStatus.BAD_REQUEST.value()) {
            return "error/404";
        }

        return "error/error";
    }
}
