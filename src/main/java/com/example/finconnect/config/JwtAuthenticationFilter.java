package com.example.finconnect.config;

import com.example.finconnect.exception.jwt.JwtTokenNotFoundException;
import com.example.finconnect.service.JwtService;
import com.example.finconnect.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    private static final List<String> EXCLUDE_URL_PATTERNS = Arrays.asList(
            "/api/v1/users",
            "/api/v1/users/authenticate"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String BEARER_PREFIX = "Bearer ";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {
            for (String pattern : EXCLUDE_URL_PATTERNS) {
                if (requestURI.matches(pattern)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        if (ObjectUtils.isEmpty(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
            throw new JwtTokenNotFoundException();
        }

        if (!ObjectUtils.isEmpty(authorization)
                && authorization.startsWith(BEARER_PREFIX)
                && securityContext.getAuthentication() == null) {
            var accessToken = authorization.substring(BEARER_PREFIX.length());
            var username = jwtService.getUsername(accessToken);
            var userDetails = userService.loadUserByUsername(username);

            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);
    }
}
