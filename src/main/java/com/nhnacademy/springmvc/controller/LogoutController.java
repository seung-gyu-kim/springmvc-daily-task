package com.nhnacademy.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(Objects.isNull(session) || Objects.isNull(session.getAttribute("id"))) {
            return "redirect:/login";
        }
        session.invalidate();
        return "redirect:/";
    }
}
