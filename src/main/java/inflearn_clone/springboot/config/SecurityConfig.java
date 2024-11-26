package inflearn_clone.springboot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 이건 모든 요청 허용한다는 뜻
        .csrf(csrf -> csrf.disable()); // CSRF 비활성화 <- 우리 사이트는 일단 이렇게 놔도 될듯

    return http.build();
  }
}
