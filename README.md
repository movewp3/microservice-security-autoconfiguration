# dwpbank MoveWP3 Spring Security OIDC Support

![Build status](https://travis-ci.com/movewp3/spring-security-oidc.svg?branch=master)

A Spring-Security-based library providing a no-frills approach to enable the verification of HTTP/REST authentication via OpenID Connect for a microservice.

## Usage

To enable, just add the following dependency to your POM:

```                                
<dependency>
    <groupId>io.dwpbank.movewp3</groupId>
    <artifactId>spring-security-oidc</artifactId>
    <version>${dwp-spring-security-oidc.version}</version>
</dependency>
```

Authentication enforcement will be enabled as soon as the property `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` is set. We recommend that you test proper authentication handling from within your unit tests. For an example, refer to `WebSecurityConfigTest`.

The `/actuator/*` endpoints are currently exempt from authentication. This will most likely change in the future.

## Contributing

Pull requests are welcome. In order to make sure that your change can be easily merged, please follow these steps:

* Develop your changes in a feature branch named `feature/...`
* Base your feature branch on `master`
* Open your pull request against `master`
* Don't forget to implement tests

In case of any questions, feel open an issue in this project to discuss intended changes upfront.

