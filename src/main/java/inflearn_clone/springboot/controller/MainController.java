package inflearn_clone.springboot.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MainController {
  @GetMapping("/")
  public String main(Model model) {
    return "main";
  }

  @GetMapping("/sign/signUp")
  public String signUp() {
    return "sign/signUp";
  }

  @GetMapping("/sign/signIn")
  public String signIn() {
    return "sign/signIn";
  }
}
