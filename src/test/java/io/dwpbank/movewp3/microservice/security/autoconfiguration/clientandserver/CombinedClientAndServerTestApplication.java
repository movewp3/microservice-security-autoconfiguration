package io.dwpbank.movewp3.microservice.security.autoconfiguration.clientandserver;

import io.dwpbank.movewp3.microservice.security.autoconfiguration.client.TestConfiguration;
import io.dwpbank.movewp3.microservice.security.autoconfiguration.server.TestController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
@Import({TestController.class, TestConfiguration.class})
public class CombinedClientAndServerTestApplication {

}
