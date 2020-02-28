package io.dwpbank.movewp3.spring.security.oidc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  @GetMapping("/foo")
  public @ResponseBody
  String foo() {
    return "foo";
  }

  @GetMapping("/actuator/bar")
  public @ResponseBody
  String bar() {
    return "bar";
  }
}
