package ru.dragonestia.loadbalancer.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Log4j2(topic = "DEBUG INTERCEPTOR")
public class DebugInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        log.info("[" + request.getMethod() + "] " + request.getRequestURI());
        var map = request.getParameterMap();

        for (String key : map.keySet()) {
            log.info("   " + key + ": " + Arrays.toString(map.get(key)));
        }
        return true;
    }
}
