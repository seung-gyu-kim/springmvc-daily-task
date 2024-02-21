package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(Objects.isNull(session) || Objects.isNull(session.getAttribute("id"))) {
            return "thymeleaf/loginForm";

        }
        return "redirect:/";    // 로그인 성공시
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request) {
        if (userRepository.matches(id, pwd)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("id", id);

            return "redirect:/";    // 로그인 성공시
        } else {
            return "thymeleaf/loginForm";
        }
    }

}
