package com.spring_cloud.eureka.client.auth;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Value("${spring.application.name}")
  private String issuer;

  @Value("${service.jwt.access-expiration}")
  private Long accessExpiration;

  private final SecretKey secretKey;

  public AuthService(@Value("${service.jwt.secret-key}")String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
  }

  public String createAccessToken(String user_id){
    return Jwts.builder()
        .claim("user_id", user_id)
        .claim("role", "ADMIN")
        .issuer(issuer)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + accessExpiration))
        .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
        .compact();

  }
}
