package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootApplication
@SpringBootTest(properties = {
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo",
    "io.dwpbank.movewp3.microservice.security.whitelist=/actuator/**,/bar/**"
})
@ContextConfiguration
public class WebSecurityConfigTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .alwaysDo(print())
        .apply(springSecurity())
        .build();
  }

  @Test
  public void unauthenticatedAccessReturnsUnauthorized() throws Exception {
    mockMvc
        .perform(get("/foo"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void whitelistedAccessReturnsNotFound() throws Exception {
    mockMvc
        .perform(get("/bar/foo"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void actuatorCanBeAccessedWithoutAuthentication() throws Exception {
    mockMvc
        .perform(get("/actuator/bar"))
        .andExpect(status().isOk());
  }
}
