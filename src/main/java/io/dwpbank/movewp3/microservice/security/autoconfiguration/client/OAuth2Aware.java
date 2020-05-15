package io.dwpbank.movewp3.microservice.security.autoconfiguration.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * A qualifier that marks a {@link WebClient.Builder} as OAuth2-aware. That means that the {@link WebClient} will request an OAuth2 access
 * token from an OAuth2 authorization server (or OpenID Connect identity provider) and submit this token as bearer token with each request.
 * For further details, refer to the documentation of {@link WebClientOauth2AutoConfiguration}.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface OAuth2Aware {

}
