package io.dwpbank.movewp3.microservice.security.autoconfiguration.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("io.dwpbank.movewp3.microservice.security")
public class MicroserviceSecurityConfigurationProperties {

  /**
   * Allow list for the
   * {@link
   * org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry}
   * used in the server security filter chain.
   */
  private List<String> allowlist = List.of("/actuator/**");

  /**
   * The default registration identifier for OAuth 2.0 clients.
   **/
  @Value("${io.dwpbank.microservice-security-autoconfiguration.default-client-registration-id:default}")
  private String defaultOauth2ClientRegistrationId;

  public List<String> getAllowlist() {
    return allowlist;
  }

  public void setAllowlist(List<String> allowlist) {
    this.allowlist = allowlist;
  }

  public String getDefaultOauth2ClientRegistrationId() {
    return defaultOauth2ClientRegistrationId;
  }

  public void setDefaultOauth2ClientRegistrationId(String defaultOauth2ClientRegistrationId) {
    this.defaultOauth2ClientRegistrationId = defaultOauth2ClientRegistrationId;
  }
}