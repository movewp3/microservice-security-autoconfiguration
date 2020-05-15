package io.dwpbank.movewp3.microservice.security.autoconfiguration.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;

public class TestTokenResponse {

  private String accessToken;

  private long expiresIn;

  private String tokenType;

  private int notBeforePolicy;

  private List<String> scope;

  public TestTokenResponse(String accessToken, long expiresIn, TokenType tokenType, int notBeforePolicy, String scopes) {
    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.tokenType = tokenType.getValue();
    this.notBeforePolicy = notBeforePolicy;
    this.scope = List.of(scopes);
  }

  @JsonProperty("access_token")
  public String getAccessToken() {
    return accessToken;
  }

  @JsonProperty("expires_in")
  public long getExpiresIn() {
    return expiresIn;
  }

  @JsonProperty("token_type")
  public String getTokenType() {
    return tokenType;
  }

  @JsonProperty("not-before-policy")
  public int getNotBeforePolicy() {
    return notBeforePolicy;
  }

  @JsonProperty("scope")
  public List<String> getScope() {
    return scope;
  }
}
