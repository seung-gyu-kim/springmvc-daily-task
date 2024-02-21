package com.nhnacademy.springmvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ElapsedTimeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("start", System.currentTimeMillis());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        request.setAttribute("stopwatch", stopWatch);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        long start = (long)request.getAttribute("start");
        long end = System.currentTimeMillis();
        log.info("time elapse : {}", end - start);
        StopWatch stopWatch = (StopWatch)request.getAttribute("stopwatch");
        stopWatch.stop();
        log.info(String.format("%s - %dms", handler.toString(), stopWatch.getTotalTimeMillis()));
        log.info(stopWatch.prettyPrint());
    }
}
