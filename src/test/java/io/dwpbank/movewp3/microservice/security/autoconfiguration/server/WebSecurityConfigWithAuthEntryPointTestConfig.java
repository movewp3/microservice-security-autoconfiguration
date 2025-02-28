package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@TestConfiguration
public class WebSecurityConfigWithAuthEntryPointTestConfig {

  @Bean
  public AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
    return new AuthenticationEntryPoint() { // No Lamda since Mockito can not handle it otherwise
      @Override
      public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
          throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
      }
    };
  }
}
