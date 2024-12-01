package inflearn_clone.springboot.config;

import inflearn_clone.springboot.domain.AdminVO;
import inflearn_clone.springboot.mappers.AdminMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final AdminMapper adminMapper;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/administrator/login").permitAll()
            .requestMatchers("/administrator/**").hasRole("ADMIN")
            .anyRequest().permitAll()
        )
        .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        .formLogin(form -> form
            .loginPage("/administrator/login")
            .loginProcessingUrl("/administrator/login-proc")
            .usernameParameter("adminId")
            .passwordParameter("adminPwd")
            .successHandler((request, response, authentication) -> {
                HttpSession session = request.getSession();
                session.setAttribute("adminId", authentication.getName());
                response.sendRedirect("/admin/dashboard");
            })
            .failureHandler((request, response, exception) -> {
                response.sendRedirect("/administrator/login?error");
            })
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/administrator/login")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        )
        .exceptionHandling(ex -> ex
            .accessDeniedPage("/administrator/login")
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
  }

  public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {
      
      String path = request.getRequestURI();
      HttpSession session = request.getSession();
      String memberType = (String) session.getAttribute("memberType");

      if ((path.startsWith("/teacher/course/") || path.equals("/teacher/modifyInfo")) 
              && !"T".equals(memberType)) {
        response.sendRedirect("/?showLogin=true");
        return;
      }

      if ((path.startsWith("/course/study") || 
           path.startsWith("/review/") ||
           path.startsWith("/like/") ||
           path.startsWith("/cart/") ||
           path.startsWith("/order/")) 
              && !"S".equals(memberType)) {
        response.sendRedirect("/?showLogin=true");
        return;
      }

      filterChain.doFilter(request, response);
    }
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new AuthenticationProvider() {
      @Override
      public Authentication authenticate(Authentication authentication)
              throws AuthenticationException {
        String adminId = authentication.getName();
        String adminPwd = authentication.getCredentials().toString();

        AdminVO admin = adminMapper.selectAdminById(adminId);
        
        if (admin == null || !adminPwd.equals(admin.getAdminPwd())) {
          throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        List<GrantedAuthority> authorities =
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        return new UsernamePasswordAuthenticationToken(
            adminId, 
            adminPwd, 
            authorities
        );
      }

      @Override
      public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
      }
    };
  }

  // PasswordEncoder 빈 등록
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 사용하여 비밀번호 암호화
  }
}
