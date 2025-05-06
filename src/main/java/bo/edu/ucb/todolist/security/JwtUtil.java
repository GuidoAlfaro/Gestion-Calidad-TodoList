package bo.edu.ucb.todolist.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "/5tcpfy7OJ1bnC2FoDa/kZ7kP3hZH1FM+zBOfd6GEbmbfAScsQTjI4eHhNJGm41bosoyMZ6Sf5qyPa/3rXxf0w==";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generar el token
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener userId desde el token
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // Obtener username si lo necesitás
    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
