package com.imtokyodev.apiliga.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class MDCInterceptor implements HandlerInterceptor {

    private static final String REQUEST_START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        long startTime = System.currentTimeMillis();
        MDC.put(REQUEST_START_TIME, String.valueOf(startTime));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long startTime = Long.parseLong(MDC.get(REQUEST_START_TIME));
        long duration = endTime - startTime;

        MDC.put("duration", String.valueOf(duration));
        logRequestDuration(duration);

        MDC.remove("requestId");
        MDC.remove(REQUEST_START_TIME);
    }

    private void logRequestDuration(long duration) {
        log.info("Request processed in " + duration + " ms");
    }

}
