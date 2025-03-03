# dwpbank MoveWP3 Microservice Security Autoconfiguration

[![Java CI](https://github.com/movewp3/microservice-security-autoconfiguration/actions/workflows/build.yml/badge.svg)](https://github.com/movewp3/microservice-security-autoconfiguration/actions/workflows/build.yml) [![Maven Central](https://img.shields.io/maven-central/v/io.dwpbank.movewp3/microservice-security-autoconfiguration)](https://search.maven.org/artifact/io.dwpbank.movewp3/microservice-security-autoconfiguration)

A Spring-Security-based library providing a no-frills approach to enable the verification of HTTP/REST authentication via OpenID Connect and
consumption of other OAuth2-protected services for a microservice.
In accordance with the stateless REST approach, session management and CSRF tokens are disabled.

## Usage

To make use if this starter, add the following dependency to your POM;

```
<dependency>
    <groupId>io.dwpbank.movewp3</groupId>
    <artifactId>microservice-security-autoconfiguration</artifactId>
    <version>${movewp3-microservice-security-autoconfiguration.version}</version>
</dependency>
```

### Server

To enable OAuth2/OpenID-Connect-based protection for your resource server, make sure to set the property
`spring.security.oauth2.resourceserver.jwt.jwk-set-uri`. We recommend that you test proper authentication handling from within your unit
tests. For an example, refer to `WebSecurityConfigTest`.

The `/actuator/*` endpoints are currently exempt from authentication by default. You can override this default by setting the property
`io.dwpbank.movewp3.microservice.security.allowlist` to a comma-separated list of endpoints to expose without the need to authenticate,
i.e.:

```
io.dwpbank.movewp3.microservice.security.allowlist=/foo/**,/bar/**
```

If required, the OAuth 2.0 Resource Server can be customized via an optional Bean "oauth2AuthenticationEntryPoint" of type
org.springframework.security.web.AuthenticationEntryPoint.

```java

@Bean
AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
  return new AuthenticationEntryPoint() {
    // TODO - implementation required
  };
}
```

### Client

To add OAuth2 support to `WebClient`, configure an OAuth2 client registration as outlined in
the [Spring Security documentation](https://docs.spring.io/spring-security/site/docs/5.3.2.RELEASE/reference/html5/#webflux-oauth2-login-sample-config).
If your registration is not named "default", additionally set the property
`iio.dwpbank.movewp3.microservice.security.default-oauth2-client-registration-id` to the ID of your client registration. Last, but not
least, annotate the `WebClient.Builder` to be injected with the `@OAuth2Aware` qualifier, such as in the following example:

```
@Autowired
@OAuth2Aware
private WebClient.Builder webClientBuilder;
```

Make sure to only submit requests via `WebClient`s created via this builder for which you are ok with the OAuth2 access token being added as
bearer token HTTP authorization header.

## Contributing

Pull requests are welcome. In order to make sure that your change can be easily merged, please follow these steps:

* Develop your changes in a feature branch named `feature/...`
* Base your feature branch on `main`
* Open your pull request against `main`
* Don't forget to implement tests

In case of any questions, feel open an issue in this project to discuss intended changes upfront.
