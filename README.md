# dwpbank MoveWP3 Microservice Security Autoconfiguration

![Build status](https://travis-ci.com/movewp3/microservice-security-autoconfiguration.svg?branch=master) ![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.dwpbank.movewp3/microservice-security-autoconfiguration/badge.svg)


A Spring-Security-based library providing a no-frills approach to enable the verification of HTTP/REST authentication via OpenID Connect and consumption of other OAuth2-protected services for a microservice.

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

To enable OAuth2/OpenID-Connect-based protection for your resource server, make sure to set the property `spring.security.oauth2.resourceserver.jwt.jwk-set-uri`. We recommend that you test proper authentication handling from within your unit tests. For an example, refer to `WebSecurityConfigTest`.

The `/actuator/*` endpoints are currently exempt from authentication. This will most likely change in the future.

### Client

To add OAuth2 support to `WebClient`, configure an OAuth2 client registration as outlined in the [Spring Security documentation](https://docs.spring.io/spring-security/site/docs/5.3.2.RELEASE/reference/html5/#webflux-oauth2-login-sample-config). If your registration is not named "default", additionally set the property `io.dwpbank.microservice-security-autoconfiguration.default-client-registration-id` to the ID of your client registration. Last, but not least, annotate the `WebClient.Builder` to be injected with the `@OAuth2Aware` qualifier, such as in the following example:

```
@Autowired
@OAuth2Aware
private WebClient.Builder webClientBuilder;
```

Make sure to only submit requests via `WebClient`s created via this builder for which you are ok with the OAuth2 access token being added as bearer token HTTP authorization header.

## Contributing

Pull requests are welcome. In order to make sure that your change can be easily merged, please follow these steps:

* Develop your changes in a feature branch named `feature/...`
* Base your feature branch on `master`
* Open your pull request against `master`
* Don't forget to implement tests

In case of any questions, feel open an issue in this project to discuss intended changes upfront.

