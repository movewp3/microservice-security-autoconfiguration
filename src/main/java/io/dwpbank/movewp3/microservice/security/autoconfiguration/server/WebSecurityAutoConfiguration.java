package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * An autoconfiguration that enables OIDC-based authentication for all HTTP endpoints (except for <code>/actuator/*</code> as soon as the
 * property <code>microservice.security.oauth2.resourceserver.jwt.jwk-set-uri</code> is set to the JWK Set URI of an OpenID Connect
 * provider.
 * <p>
 * In case both {@link OAuth2ClientAutoConfiguration} and this auto-configuration are applied onto an application, both would try and add a
 * {@link WebSecurityConfigurerAdapter}. To avoid such a conflict, the one defined in this class will take precedence over the one provided
 * via {@link OAuth2ClientAutoConfiguration}.
 */
@Configuration
@EnableWebSecurity
@AutoConfigureBefore(OAuth2ClientAutoConfiguration.class)
@ConditionalOnProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
class WebSecurityAutoConfiguration {

  @Bean
  WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
    return new OpinionatedWebSecurityConfigurerAdapter();
  }
}

