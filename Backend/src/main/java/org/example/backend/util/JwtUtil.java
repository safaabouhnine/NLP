package org.example.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;


    /**
     * Méthode pour obtenir la clé de signature à partir de la clé secrète.
     * La clé doit être encodée en Base64.
     */

    // Liste noire des tokens invalidés
    private final Set<String> tokenBlacklist = new HashSet<>();

    public void addToBlacklist(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }



    public SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Décoder la clé secrète en Base64
        return Keys.hmacShaKeyFor(keyBytes); // Créer une clé HMAC-SHA
    }

    /**
     * Génère un token JWT pour un email donné.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Définit le subject (email)
                .setIssuedAt(new Date()) // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature avec HMAC-SHA256
                .compact();
    }


    /**
     * Valide un token JWT en comparant l'email et en vérifiant l'expiration.
     */
    public boolean validateToken(String token, String email) {
        try {
            String subject = extractEmail(token);
            return (subject.equals(email) && !isTokenExpired(token) && !isTokenBlacklisted(token));
        } catch (Exception e) {
            return false; // Retourne false si le token est invalide ou expiré
        }
    }


    /**
     * Extrait l'email (subject) du token JWT.
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject(); // Récupère le champ "sub" (subject) du JWT
    }

    /**
     * Extrait le username du token JWT.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Vérifie si le token est expiré.
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Extrait toutes les claims (payload) du token JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utiliser la clé décodée
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Valide un token JWT et retourne l'email extrait.
     */
    public String validateTokenAndGetEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Utiliser la clé décodée
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // Récupère l'email depuis le token
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return null; // Token invalide ou expiré
        }
    }
}
