package com.spring_cloud.eureka.client.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LocalJwtAuthenticationFilter implements GlobalFilter {

  @Value("${service.jwt.secret-key}")
  private String secretKey;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    // 로그인 페이지로 접근하는건 인증 안 함.
    if(path.equals("/auth/signIn")){
      return chain.filter(exchange);
    }

    // 헤더에서 토큰값 가져오기.
    String token = extractToken(exchange);

    // 토큰에 문제가 있으면 여기서 오류 던짐.
    if(token == null || !validateToken(token)){
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    return chain.filter(exchange); // 넘기기.
  }

  private String extractToken(ServerWebExchange exchange) {
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    // 값이 있으면서 bearer로 시작하면
    if(authHeader != null && authHeader.startsWith("Bearer ")) {
       return authHeader.substring(7); // 토큰만 전달
    }

    return null; // 토큰을 못받았을 때.
  }

  // 토큰 해석 후 로그인 메서드
  private boolean validateToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build().parseSignedClaims(token);
      log.info("#####payload :: " + claimsJws.getPayload().toString());

      // 추가적인 검증 로직 (예: 토큰 만료 여부 확인 등)을 여기에 추가할 수 있습니다.
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}

