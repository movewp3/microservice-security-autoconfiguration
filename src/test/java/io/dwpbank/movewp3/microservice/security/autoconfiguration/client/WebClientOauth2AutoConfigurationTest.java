package io.dwpbank.movewp3.microservice.security.autoconfiguration.client;

import static org.assertj.core.api.Assertions.assertThat;

import io.fabric8.mockwebserver.DefaultMockServer;
import io.fabric8.mockwebserver.utils.ResponseProviders;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBootApplication
@EnableWebSecurity
public class WebClientOauth2AutoConfigurationTest {

  @Autowired
  private WebClient.Builder webClientBuilder;

  @Autowired
  @OAuth2Aware
  private WebClient.Builder oauth2WebClientBuilder;

  @Autowired
  private DefaultMockServer mockServer;

  @Test
  void testOauth2WebClientSubmitsRequestsWithBearerToken() {
    mockServer
        .expect()
        .post()
        .withPath("/oauth2/token")
        .andReply(ResponseProviders.of(200, testTokenResponse(), Map.of("content-type", "application/json")))
        .once();

    var responseEntity = oauth2WebClientBuilder
        .baseUrl("http://foo")
        .exchangeFunction(clientRequest -> {
          var authorizationHeaders = clientRequest.headers().get("Authorization");
          assertThat(authorizationHeaders).hasSize(1);
          var authorizationHeader = authorizationHeaders.get(0);
          assertThat(authorizationHeader).startsWith("Bearer ");

          return Mono.just(ClientResponse.create(HttpStatus.OK).build());
        })
        .build()
        .get()
        .uri("/bar")
        .retrieve()
        .toBodilessEntity()
        .block();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testRegularWebClientOmitsBearerToken() {
    var responseEntity = webClientBuilder
        .baseUrl("http://foo")
        .exchangeFunction(clientRequest -> {
          assertThat(clientRequest.headers()).doesNotContainKeys("Authorization");
          return Mono.just(ClientResponse.create(HttpStatus.OK).build());
        })
        .build()
        .get()
        .uri("/baz")
        .retrieve()
        .toBodilessEntity()
        .block();

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  private TestTokenResponse testTokenResponse() {
    return new TestTokenResponse("test", 120, TokenType.BEARER, 0, "test");
  }
}
