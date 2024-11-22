package com.name.vehicleregistration.filter;

import ch.qos.logback.core.util.StringUtil;
import com.name.vehicleregistration.service.UserService;
import com.name.vehicleregistration.service.impl.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService profileService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        try {
            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (!StringUtil.isNullOrEmpty(userEmail) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = profileService.userDetailsService().loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("Error - El token JWT ha expirado: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código 401
            response.getWriter().write("El token JWT ha expirado. Por favor, inicie sesión nuevamente.");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("Error - El token JWT no es válido: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Código 400
            response.getWriter().write("El token JWT no es válido.");
        } catch (Exception e) {
            log.error("Error - Token JWT inválido: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código 401
            response.getWriter().write("Token JWT inválido. No se pudo validar la firma.");
        }
    }
}
