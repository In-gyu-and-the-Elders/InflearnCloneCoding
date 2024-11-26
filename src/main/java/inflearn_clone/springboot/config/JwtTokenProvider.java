package inflearn_clone.springboot.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final long expirationTime = 1000 * 60 * 60;

  // JWT 생성
  public String createToken(String memberId, String memberType) {
    return Jwts.builder()
        .setSubject(memberId)
        .claim("memberType", memberType)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(key)
        .compact();
  }

  // JWT 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // JWT에서 사용자 정보 추출
  public String getMemberId(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}

