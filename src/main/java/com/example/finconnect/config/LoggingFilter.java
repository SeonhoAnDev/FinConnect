package com.example.finconnect.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        logger.info("Request URI: " + requestURI);

        // 각 단계별로 로그 출력
        logger.info("Step 1: HttpMethod.POST");
        logger.info("Step 2: /api/*/users");
        logger.info("Step 3: /api/*/users/authenticate");

        // 필터 체인의 다음 필터로 제어를 전달
        filterChain.doFilter(request, response);
    }
}
