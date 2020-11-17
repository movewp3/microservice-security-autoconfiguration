package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ImportAutoConfiguration({WebSecurityConfig.class})
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
    "io.dwpbank.movewp3.microservice.security.allowlist=/get/**,/post/**,/allowlisted/**"})
class WebSecurityConfigAllowlistTest {

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
  void allowlistedPostAccessReturnsOk() throws Exception {
    mockMvc
        .perform(post("/post/bar")
            .contentType("text/plain;charset=UTF-8")
            .content("someText"))
        .andExpect(status().isOk());
  }

  @Test
  void allowlistedGetAccessReturnsOk() throws Exception {
    mockMvc
        .perform(get("/get/bar"))
        .andExpect(status().isOk());
  }

  @Test
  void allowlistedButNotImplementedAccessReturnsNotFound() throws Exception {
    mockMvc
        .perform(get("/allowlisted/foo"))
        .andExpect(status().isNotFound());
  }

  @Test
  void actuatorCanBeAccessed() throws Exception {
    mockMvc
        .perform(get("/actuator/bar"))
        .andExpect(status().isUnauthorized());
  }
}
