package com.innowise.userservice.security.filter;

import com.innowise.userservice.common.constants.Headers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalApiKeyFilter extends OncePerRequestFilter {

    public static final String MISSING_INTERNAL_API_KEY = "{\"error\": \"Invalid or missing internal API key\"}";

    public static final String INTERNAL_SERVICE_ROLE = "ROLE_INTERNAL_SERVICE";

    public static final String INTERNAL_SERVICE_PRINCIPAL = "internal-service";

    public static final String INTERNAL_USERS_PATH = "/internal/users";

    @Value("${internal.api-key}")
    private String validInternalApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith(INTERNAL_USERS_PATH)) {
            String providedKey = request.getHeader(Headers.INTERNAL_KEY);

            if (validInternalApiKey != null && validInternalApiKey.equals(providedKey)) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        INTERNAL_SERVICE_PRINCIPAL,
                        null,
                        List.of(new SimpleGrantedAuthority(INTERNAL_SERVICE_ROLE))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(MISSING_INTERNAL_API_KEY);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}