package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ImportAutoConfiguration({WebSecurityConfig.class})
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
    "io.dwpbank.movewp3.microservice.security.whitelist=/bar/**,/whitelisted/**"})
class WebSecurityConfigWhitelistTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .alwaysDo(print())
        .apply(springSecurity())
        .build();
  }

  @Test
  void unauthenticatedAccessReturnsUnauthorized() throws Exception {
    mockMvc
        .perform(get("/foo"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void whitelistedAccessReturnsOk() throws Exception {
    mockMvc
        .perform(get("/bar/foo"))
        .andExpect(status().isOk());
  }

  @Test
  void whitelistedButNotImplementedAccessReturnsNotFound() throws Exception {
    mockMvc
        .perform(get("/whitelisted/foo"))
        .andExpect(status().isNotFound());
  }

  @Test
  void actuatorCanNotBeAccessedIfNotWhitelisted() throws Exception {
    mockMvc
        .perform(get("/actuator/bar"))
        .andExpect(status().isUnauthorized());
  }
}
