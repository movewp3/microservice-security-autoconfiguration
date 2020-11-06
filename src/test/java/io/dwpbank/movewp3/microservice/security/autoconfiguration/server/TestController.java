package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

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

  @GetMapping("bar/foo")
  public @ResponseBody
  String barfoo() {
    return "barfoo";
  }

  @GetMapping("/actuator/bar")
  public @ResponseBody
  String bar() {
    return "bar";
  }
}
