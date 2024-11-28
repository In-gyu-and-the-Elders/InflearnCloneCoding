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
  public String createToken(String memberId, String memberType, String name, String email, String phone) {
    return Jwts.builder()
        .setSubject(memberId)
        .claim("memberType", memberType)
        .claim("name", name)
        .claim("email", email)
        .claim("phone", phone)
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

  // JWT memberId 추출
  public String getMemberId(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  // JWT memberId 추출
  public String getMemberType(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("memberType", String.class);
  }

  // JWT name 추출
  public String getName(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("name", String.class);
  }

  // JWT email 추출
  public String getEmail(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("email", String.class);
  }

  // JWT phoneNum 추출
  public String getPhone(String token) {
    Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    return claims.get("phone", String.class);
  }
}

