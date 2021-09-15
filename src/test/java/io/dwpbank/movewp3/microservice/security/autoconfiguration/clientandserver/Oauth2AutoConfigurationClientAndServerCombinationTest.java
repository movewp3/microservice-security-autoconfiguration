package io.dwpbank.movewp3.microservice.security.autoconfiguration.clientandserver;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.client.WebClientOauth2AutoConfigurationTest;
import io.dwpbank.movewp3.microservice.security.autoconfiguration.server.WebSecurityConfigDefaultTest;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;


public class Oauth2AutoConfigurationClientAndServerCombinationTest {

  @Nested
  @SpringBootTest(properties = {
      "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
  })
  @EnableAutoConfiguration
  class ClientTest extends WebClientOauth2AutoConfigurationTest {

  }

  @Nested
  @SpringBootTest(properties = {
      "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
  })
  @EnableAutoConfiguration
  class ResourceServerTest extends WebSecurityConfigDefaultTest {

  }
}
