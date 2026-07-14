package org.example.reviewservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) {
        try{

        String token = extractToken(request);


        if (token != null && validateToken(token)) {
            Long userId = extractUserId(token);


            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userId.toString(), null, new ArrayList<>()
            );


            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);}
        catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); 
        }
        return null;
    }


    private Long extractUserId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("sub", String.class));
    }

    private Boolean validateToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
         //   LOGGER.info("Успешно валидирован пользователь " + claims.get("id", String.class));
            return true;
        } catch (Exception e) {
            //LOGGER.warning("Ошибка валидации: " + e.getMessage());
            return false;
        }
    }
}