package com.nhnacademy.springmvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Content-type: {}", request.getHeader("Content-type"));
        HttpSession session = request.getSession(false);
        if(Objects.isNull(session) || Objects.isNull(session.getAttribute("id"))) {
            response.sendRedirect("/login");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
