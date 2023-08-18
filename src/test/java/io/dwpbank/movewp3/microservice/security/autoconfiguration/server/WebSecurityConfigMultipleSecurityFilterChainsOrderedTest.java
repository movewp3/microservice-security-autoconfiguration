package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.server.WebSecurityConfigMultipleSecurityFilterChainsOrderedTest.AdditionalHttpSecurityConfig;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * Tests combination of {@link WebSecurityAutoConfiguration} in combination with additional {@link SecurityFilterChain}s. For this test an
 * additional {@link SecurityFilterChain} is included ({@link AdditionalHttpSecurityConfig}), which defines basic auth for specific request
 * matchers, and for these paths, takes precedence over {@link WebSecurityAutoConfiguration}, while all other paths should still use auth
 * defined in {@link WebSecurityAutoConfiguration}
 *
 * @see <a
 * href="https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#_multiple_httpsecurity_instances">https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#_multiple_httpsecurity_instances</a>
 */
@WebMvcTest
@ImportAutoConfiguration({WebSecurityAutoConfiguration.class})
@Import(AdditionalHttpSecurityConfig.class)
@TestPropertySource(properties = {"spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://foo"})
class WebSecurityConfigMultipleSecurityFilterChainsOrderedTest {

  private static final String TESTUSERNAME = UUID.randomUUID().toString();
  private static final String TESTPASSWORD = UUID.randomUUID().toString();

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
  void unauthenticatedAccessReturnsUnauthorizedForDefaultChain() throws Exception {
    mockMvc
        .perform(get("/foo"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void unauthenticatedAccessReturnsUnauthorizedForAdditionalChain() throws Exception {
    mockMvc
        .perform(get("/get/bar"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void basicAuthAccessReturnsUnauthorizedForDefaultChain() throws Exception {
    mockMvc
        .perform(get("/foo")
            .with(httpBasic(TESTUSERNAME, TESTPASSWORD))
        )
        .andExpect(status().isUnauthorized());
  }

  @Test
  void basicAuthAccessReturnsOkForAdditionalChain() throws Exception {
    mockMvc
        .perform(get("/get/bar")
            .with(httpBasic(TESTUSERNAME, TESTPASSWORD))
        )
        .andExpect(status().isOk())
        .andExpect(content().string("get/bar"));
  }

  @Test
  void actuatorCanBeAccessedViaGetWithoutAuthentication() throws Exception {
    mockMvc
        .perform(get("/actuator/get"))
        .andExpect(status().isOk());
  }

  @Test
  void actuatorCanBeAccessedViaPostWithoutAuthentication() throws Exception {
    mockMvc
        .perform(post("/actuator/post")
            .contentType("text/plain;charset=UTF-8")
            .content("someText"))
        .andExpect(status().isOk());
  }

  @TestConfiguration
  static class AdditionalHttpSecurityConfig {

    @Bean
    @Order(1) // Vorrang vor WebSecurityAutoConfiguration
    public SecurityFilterChain testBasicAuthFilterChain(HttpSecurity http) throws Exception {
      http
          .securityMatchers(matchers -> matchers.requestMatchers("/get/**"))
          .authorizeHttpRequests(authorize -> authorize
              .anyRequest().hasRole("TEST")
          )
          .httpBasic(Customizer.withDefaults())
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return http.build();
    }

    @Bean
    public UserDetailsService testUserDetailsService() {
      var userDetails = User.builder()
          .username(TESTUSERNAME)
          .password(passwordEncoder().encode(TESTPASSWORD))
          .roles("TEST")
          .build();
      return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
  }
}
