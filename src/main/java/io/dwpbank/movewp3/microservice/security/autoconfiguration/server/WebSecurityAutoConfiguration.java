package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * An autoconfiguration that enables OIDC-based authentication for all HTTP endpoints (except for <code>/actuator/*</code> as soon as the
 * property <code>microservice.security.oauth2.resourceserver.jwt.jwk-set-uri</code> is set to the JWK Set URI of an OpenID Connect
 * provider.
 * <p>
 * In case both {@link OAuth2ClientAutoConfiguration} and this auto-configuration are applied onto an application, both would try and add a
 * {@link SecurityFilterChain}. To avoid such a conflict, the one defined in this class will take precedence over the one provided via
 * {@link OAuth2ClientAutoConfiguration}.
 */
@Configuration
@EnableWebSecurity
@AutoConfigureBefore(OAuth2ClientAutoConfiguration.class)
@ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
@ConditionalOnWebApplication
public class WebSecurityAutoConfiguration {

  @Value("${io.dwpbank.movewp3.microservice.security.allowlist:/actuator/**}")
  private List<String> allowlist;

  @Autowired
  private HandlerMappingIntrospector introspector;

  @Bean
  SecurityFilterChain oidcResourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .authorizeHttpRequests()
        .requestMatchers(
            allowlist
                .stream()
                .map(path -> new MvcRequestMatcher(introspector, path))
                .toArray(MvcRequestMatcher[]::new)
        )
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer().jwt();
    // @formatter:on

    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.csrf().disable();

    return http.build();
  }
}

