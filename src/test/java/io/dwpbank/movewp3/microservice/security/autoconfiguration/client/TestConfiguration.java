package io.dwpbank.movewp3.microservice.security.autoconfiguration.client;

import io.fabric8.mockwebserver.DefaultMockServer;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class TestConfiguration {

  @Bean
  public DefaultMockServer mockServer() {
    var defaultMockServer = new DefaultMockServer();
    defaultMockServer.start();
    return defaultMockServer;
  }

  @Bean
  public ClientRegistration clientRegistration(DefaultMockServer defaultMockServer) {
    return ClientRegistration.withRegistrationId("default")
        .clientId("client-1")
        .clientSecret("secret")
        .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope("read", "write")
        .tokenUri(defaultMockServer.url("/oauth2/token"))
        .build();
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository(List<ClientRegistration> registrations) {
    return new InMemoryClientRegistrationRepository(registrations);
  }
}
