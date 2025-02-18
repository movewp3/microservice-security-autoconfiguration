package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.server.WebSecurityConfigMultipleSecurityFilterChainsOrderedTest.AdditionalHttpSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ImportAutoConfiguration({WebSecurityAutoConfiguration.class})
@Import(AdditionalHttpSecurityConfig.class)
@TestPropertySource(properties = {"spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo"})
@ContextConfiguration(classes = WebSecurityConfigWithAuthEntryPointTestConfig.class)
class WebSecurityConfigWithAuthEntryPointTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @MockitoSpyBean
  private AuthenticationEntryPoint oauth2AuthenticationEntryPoint;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .alwaysDo(print())
        .apply(springSecurity())
        .build();
  }

  @Test
  void unauthorizedRequestToPublicEndpointDoesNotInvokeAuthEntryPoint() throws Exception {
    mockMvc
        .perform(get("/actuator/get"))
        .andExpect(status().isOk());

    verify(oauth2AuthenticationEntryPoint, never()).commence(any(), any(), any());
  }

  @Test
  void unauthorizedRequestToRestrictedEndpointInvokesAuthEntryPoint() throws Exception {
    mockMvc
        .perform(get("/foo"))
        .andExpect(status().isUnauthorized());

    verify(oauth2AuthenticationEntryPoint).commence(any(), any(), any());
  }

}
