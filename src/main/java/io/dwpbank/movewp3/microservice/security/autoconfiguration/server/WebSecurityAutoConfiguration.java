package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.config.MicroserviceSecuritySettings;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
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
@AutoConfiguration
@EnableWebSecurity
@AutoConfigureBefore(OAuth2ClientAutoConfiguration.class)
@ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
@ConditionalOnWebApplication
@EnableConfigurationProperties({MicroserviceSecuritySettings.class})
public class WebSecurityAutoConfiguration {

  @Bean
  SecurityFilterChain oidcResourceServerSecurityFilterChainReduced(
      HttpSecurity http,
      MicroserviceSecuritySettings microserviceSecuritySettings,
      HandlerMappingIntrospector introspector,
      @Qualifier("oauth2AuthenticationEntryPoint") Optional<AuthenticationEntryPoint> oauth2AuthenticationEntryPoint) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
            authorizationManagerRequestMatcherRegistry.requestMatchers(microserviceSecuritySettings.getAllowlist()
                    .stream()
                    .map(path -> new MvcRequestMatcher(introspector, path))
                    .toArray(MvcRequestMatcher[]::new))
                .permitAll()
                .anyRequest()
                .authenticated())
        .oauth2ResourceServer(oauth2 -> {
          oauth2.jwt(Customizer.withDefaults());
          oauth2AuthenticationEntryPoint.ifPresent(oauth2::authenticationEntryPoint);
        })
        .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .exceptionHandling(Customizer.withDefaults())
        .build();
  }
}

