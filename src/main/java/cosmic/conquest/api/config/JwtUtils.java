package cosmic.conquest.api.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Key;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${project.cookie.secret}")
    private String jwtSecret;

    @Value("${project.cookie.name}")
    private String jwtCookie;

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getJwtFromCookies(HttpServletRequest request) throws IOException {
        var token = new BufferedReader(request.getReader())
                .lines()
                .collect(Collectors.joining("\n"));
        return token;
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return false;
    }
}
