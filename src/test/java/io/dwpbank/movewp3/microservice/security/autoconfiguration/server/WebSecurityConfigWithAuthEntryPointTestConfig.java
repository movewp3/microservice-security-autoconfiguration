package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;

@TestConfiguration
public class WebSecurityConfigWithAuthEntryPointTestConfig {

  @Bean
  public AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
    return (request, response, authException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

}
