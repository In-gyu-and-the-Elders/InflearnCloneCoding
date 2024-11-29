package inflearn_clone.springboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignNormalController {
  @GetMapping("/signUp")
  public String signUp() {
    return "sign/signUp";
  }

  @GetMapping("/signIn")
  public String signIn() {
    return "sign/signIn";
  }
}
