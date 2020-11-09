package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * A {@link WebSecurityConfigurerAdapter} that enables OIDC-based authentication for all HTTP endpoints (except for
 * <code>/actuator/*</code> as soon as the property <code>microservice.security.oauth2.resourceserver.jwt.jwk-set-uri</code>
 * is set to the JWK Set URI of an OpenID Connect provider.
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Value("${io.dwpbank.movewp3.microservice.security.whitelist:#{null}}")
  private Optional<String[]> whitelist;

  @Override
  public void configure(WebSecurity web) throws Exception {
    whitelist.ifPresent(web.ignoring()::antMatchers);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .authorizeRequests()
          .antMatchers("/actuator/**")
            .permitAll()
          .anyRequest()
              .authenticated()
            .and()
              .oauth2ResourceServer().jwt();
    // @formatter:on

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }
}

