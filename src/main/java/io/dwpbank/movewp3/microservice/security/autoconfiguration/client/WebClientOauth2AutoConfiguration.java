package io.dwpbank.movewp3.microservice.security.autoconfiguration.client;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.config.MicroserviceSecurityConfigurationProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Boot Autoconfiguration, which adds a bean for an OAuth2-aware {@link WebClient.Builder} that can be used to obtain clients suited
 * to interact with an OAuth2-protected resource server and will automatically obtain OAuth2 access tokens required to access the resource
 * server.
 * <p>
 * As a prerequisite the OAuth2 client registration needs to be configured using standard Spring Security means, such as:
 * </p>
 * <pre>
 *   {@code
 *   spring:
 *    security:
 *      oauth2:
 *        client:
 *          registration:
 *            default: # &lt;-- client registration id
 *              authorization-grant-type: client_credentials
 *              client-id: my-client-id
 *              client-secret: secret
 *              provider: example-provider
 *          provider:
 *           example-provider:
 *             token-uri: https://oauth2.example.com/oauth2/token
 *  }
 * </pre>
 *
 * <p>
 * If your client registration id is called <code>default</code> as in the example, that's all you need to do. If you choose to pick a
 * different name, make sure to also set the property
 * <code>io.dwpbank.microservice-security-autoconfiguration.default-client-registration-id</code> to your client registration id.
 * </p>
 * <p>
 * To obtain an OAuth2-enabled {@link WebClient.Builder}, use <code>@OAuth2Aware</code> when injecting the builder as in (or equivalent
 * constructor-based injection):
 * </p>
 * <pre>
 * {@literal @Autowired}
 * {@literal @OAuth2Aware}
 * private WebClient.Builder webClientBuilder;
 * </pre>
 */
@AutoConfiguration
@ConditionalOnBean(ClientRegistrationRepository.class)
@AutoConfigureAfter({WebClientAutoConfiguration.class, OAuth2ClientAutoConfiguration.class, JacksonAutoConfiguration.class})
@EnableConfigurationProperties({MicroserviceSecurityConfigurationProperties.class})
public class WebClientOauth2AutoConfiguration {

  @Bean
  @Scope("prototype")
  @OAuth2Aware
  public WebClient.Builder oauth2AwareWebClientBuilder(
      WebClient.Builder webClientBuilder,
      ServletOAuth2AuthorizedClientExchangeFilterFunction oAuth2ExchangeFilterFunction
  ) {
    var oauth2WebClientBuilder = webClientBuilder.clone();
    oauth2WebClientBuilder.apply(oAuth2ExchangeFilterFunction.oauth2Configuration());
    return oauth2WebClientBuilder;
  }

  @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientService clientService
  ) {
    OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .build();

    AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    return authorizedClientManager;
  }

  @Bean
  public static ServletOAuth2AuthorizedClientExchangeFilterFunction oAuth2ExchangeFilterFunction(
      MicroserviceSecurityConfigurationProperties microserviceSecurityConfigurationProperties,
      OAuth2AuthorizedClientManager authorizedClientManager,
      ClientRegistrationRepository clientRegistrationRepository) {
    final var filterFunction = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
        authorizedClientManager);

    if (clientRegistrationRepository.findByRegistrationId(
        microserviceSecurityConfigurationProperties.getDefaultOauth2ClientRegistrationId()) == null) {
      throw new IllegalArgumentException(
          String.format("The configured OAuth2 default client registration id \"%s\" does not seem to exist. " +
                  "Please make sure that the client registration id of your OAuth2 client configured below " +
                  "spring.security.oauth2.client.registration matches what you have configured for " +
                  "io.dwpbank.movewp3.microservice.security.default-oauth2-client-registration-id",
              microserviceSecurityConfigurationProperties.getDefaultOauth2ClientRegistrationId())
      );
    }

    filterFunction.setDefaultClientRegistrationId(microserviceSecurityConfigurationProperties.getDefaultOauth2ClientRegistrationId());
    return filterFunction;
  }
}
