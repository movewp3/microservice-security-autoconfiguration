package io.dwpbank.movewp3.microservice.security.autoconfiguration.webapplicationtypenone;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
    "spring.main.web-application-type=none"
})
class WebApplicationTypeNoneTest {

  @Test
  void contextLoads() {
  }

}
