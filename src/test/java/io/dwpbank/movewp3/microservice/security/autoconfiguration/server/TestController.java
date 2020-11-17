package io.dwpbank.movewp3.microservice.security.autoconfiguration.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

  @GetMapping("/foo")
  public @ResponseBody
  String doFoo() {
    return "foo";
  }

  @PostMapping("post/bar")
  public @ResponseBody
  String doPost(@RequestBody String body) {
    return body;
  }

  @GetMapping("get/bar")
  public @ResponseBody
  String doGet() {
    return "get/bar";
  }

  @GetMapping("/actuator/get")
  public @ResponseBody
  String doGetActuator() {
    return "bar";
  }

  @PostMapping("actuator/post")
  public @ResponseBody
  String doPostActuator(@RequestBody String body) {
    return body;
  }
}
