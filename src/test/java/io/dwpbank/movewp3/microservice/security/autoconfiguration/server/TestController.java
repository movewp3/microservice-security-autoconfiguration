package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  @GetMapping("/foo")
  public @ResponseBody
  String foo() {
    return "foo";
  }

  @PostMapping("post/bar")
  public @ResponseBody
  String post(@RequestBody String body) {
    return body;
  }

  @PutMapping("put/bar")
  public @ResponseBody
  void put(@RequestBody String body) {
  }

  @GetMapping("get/bar")
  public @ResponseBody
  String get() {
    return "get/bar";
  }

  @GetMapping("/actuator/bar")
  public @ResponseBody
  String bar() {
    return "bar";
  }
}
